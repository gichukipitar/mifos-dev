package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import com.fineract.mifos.mifos_core.batch.dtos.Header;
import com.fineract.mifos.mifos_core.batch.exception.ErrorInfo;
import com.fineract.mifos.mifos_core.infrastructure.core.dto.ApiParameterError;
import com.fineract.mifos.mifos_core.infrastructure.core.serialization.GoogleGsonSerializerHelper;
import com.google.gson.Gson;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.core.ResolvableType.forClassWithGenerics;

/**
 * Provides an Error Handler method that returns an object of type {@link ErrorInfo} to the CommandStrategy which raised
 * the exception. This class uses various subclasses of RuntimeException to check the kind of exception raised and
 * provide appropriate status and error codes for each one of the raised exception.
 *
 * @author Peter Gichuki
 */
@Component
@Slf4j
@AllArgsConstructor
public final class ErrorHandler {

    private static final Gson JSON_HELPER = GoogleGsonSerializerHelper.createGsonBuilder(true).create();

    private enum PessimisticLockingFailureCode {

        ROLLBACK("40"), // Transaction rollback
        DEADLOCK("60"), // Oracle: deadlock
        HY00("HY", "Lock wait timeout exceeded"), // MySql deadlock HY00
        ;

        private final String code;
        private final String msg;

        PessimisticLockingFailureCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        PessimisticLockingFailureCode(String code) {
            this(code, null);
        }

        private static Throwable match(Throwable t) {
            Throwable rootCause = ExceptionUtils.getRootCause(t);
            return rootCause instanceof SQLException sqle && Arrays.stream(values()).anyMatch(e -> e.matches(sqle)) ? rootCause : null;
        }

        private boolean matches(SQLException ex) {
            return code.equals(getSqlClassCode(ex)) && (msg == null || ex.getMessage().contains(msg));
        }

        @Nullable
        private static String getSqlClassCode(SQLException ex) {
            String sqlState = ex.getSQLState();
            if (sqlState == null) {
                SQLException nestedEx = ex.getNextException();
                if (nestedEx != null) {
                    sqlState = nestedEx.getSQLState();
                }
            }
            return sqlState != null && sqlState.length() > 2 ? sqlState.substring(0, 2) : sqlState;
        }
    }

    @Autowired
    private final ApplicationContext ctx;

    @Autowired
    private final DefaultExceptionMapper defaultExceptionMapper;

    @NotNull
    public <T extends RuntimeException> ExceptionMapper<T> findMostSpecificExceptionHandler(T exception) {
        Class<?> clazz = exception.getClass();
        do {
            Set<String> exceptionMappers = createSet(ctx.getBeanNamesForType(forClassWithGenerics(ExceptionMapper.class, clazz)));
            Set<String> fineractErrorMappers = createSet(ctx.getBeanNamesForType(FineractExceptionMapper.class));
            SetUtils.SetView<String> intersection = SetUtils.intersection(exceptionMappers, fineractErrorMappers);
            if (!intersection.isEmpty()) {
                // noinspection unchecked
                return (ExceptionMapper<T>) ctx.getBean(intersection.iterator().next());
            }
            if (!exceptionMappers.isEmpty()) {
                // noinspection unchecked
                return (ExceptionMapper<T>) ctx.getBean(exceptionMappers.iterator().next());
            }
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Exception.class));
        // noinspection unchecked
        return (ExceptionMapper<T>) defaultExceptionMapper;
    }

    /**
     * Returns an object of ErrorInfo type containing the information regarding the raised error.
     *
     * @param exception
     * @return ErrorInfo
     */
    public ErrorInfo handle(@NotNull RuntimeException exception) {
        ExceptionMapper<RuntimeException> exceptionMapper = findMostSpecificExceptionHandler(exception);
        Response response = exceptionMapper.toResponse(exception);
        MultivaluedMap<String, Object> headers = response.getHeaders();
        Set<Header> batchHeaders = headers == null ? null
                : headers.keySet().stream().map(e -> new Header(e, response.getHeaderString(e))).collect(Collectors.toSet());
        Integer errorCode = exceptionMapper instanceof FineractExceptionMapper ? ((FineractExceptionMapper) exceptionMapper).errorCode()
                : null;
        Object msg = response.getEntity();
        return new ErrorInfo(response.getStatus(), errorCode, msg instanceof String ? (String) msg : JSON_HELPER.toJson(msg), batchHeaders);
    }

    public static RuntimeException getMappable(@NotNull Throwable thr) {
        return getMappable(thr, null, null, null);
    }

    public static RuntimeException getMappable(@NotNull Throwable thr, String msgCode, String defaultMsg) {
        return getMappable(thr, msgCode, defaultMsg, null);
    }

    public static RuntimeException getMappable(@NotNull Throwable t, String msgCode, String defaultMsg, String param,
                                               final Object... defaultMsgArgs) {
        String msg = defaultMsg == null ? t.getMessage() : defaultMsg;
        String codePfx = "error.msg" + (param == null ? "" : ("." + param));
        Object[] args = defaultMsgArgs == null ? new Object[] { t } : defaultMsgArgs;

        Throwable cause;
        if ((cause = PessimisticLockingFailureCode.match(t)) != null) {
            return new PessimisticLockingFailureException(msg, cause); // deadlock
        }
        if (t instanceof NestedRuntimeException nre) {
            cause = nre.getMostSpecificCause();
            msg = defaultMsg == null ? cause.getMessage() : defaultMsg;
            if (nre instanceof NonTransientDataAccessException) {
                msgCode = msgCode == null ? codePfx + ".data.integrity.issue" : msgCode;
                return new PlatformDataIntegrityException(msgCode, msg, param, args);
            } else if (cause instanceof OptimisticLockException) {
                return (RuntimeException) cause;
            }
        }
        if (t instanceof ValidationException) {
            msgCode = msgCode == null ? codePfx + ".validation.error" : msgCode;
            return new PlatformApiDataValidationException(List.of(ApiParameterError.parameterError(msgCode, msg, param, defaultMsgArgs)));
        }
        if (t instanceof jakarta.persistence.OptimisticLockException) {
            return (RuntimeException) t;
        }
        if (t instanceof PersistenceException) {
            msgCode = msgCode == null ? codePfx + ".persistence.error" : msgCode;
            return new PlatformDataIntegrityException(msgCode, msg, param, args);
        }
        if (t instanceof AuthenticationException) {
            msgCode = msgCode == null ? codePfx + ".authentication.error" : msgCode;
            return new PlatformDataIntegrityException(msgCode, msg, param, args);
        }
        if (t instanceof ParseException) {
            msgCode = msgCode == null ? codePfx + ".parse.error" : msgCode;
            return new PlatformDataIntegrityException(msgCode, msg, param, args);
        }
        if (t instanceof RuntimeException re) {
            return re;
        }
        return new RuntimeException(msg, t);
    }

    private static <T> Set<T> createSet(T[] array) {
        if (array == null) {
            return Set.of();
        } else {
            return Set.of(array);
        }
    }

    public static Throwable findMostSpecificException(Exception exception) {
        Throwable mostSpecificException = exception;
        while (mostSpecificException.getCause() != null) {
            mostSpecificException = mostSpecificException.getCause();
        }
        return mostSpecificException;
    }

}
