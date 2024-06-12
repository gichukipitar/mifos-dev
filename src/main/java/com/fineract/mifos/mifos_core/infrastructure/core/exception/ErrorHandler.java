package com.fineract.mifos.mifos_core.infrastructure.core.exception;

import com.fineract.mifos.mifos_core.batch.exception.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

}
