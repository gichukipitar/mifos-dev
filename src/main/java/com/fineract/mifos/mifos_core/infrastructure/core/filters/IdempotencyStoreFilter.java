package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class IdempotencyStoreFilter extends OncePerRequestFilter {

    private final FineractRequestContextHolder fineractRequestContextHolder;
    private final IdempotencyStoreHelper helper;
    private final FineractProperties fineractProperties;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        Mutable<ContentCachingResponseWrapper> wrapper = new MutableObject<>();
        if (helper.isAllowedContentTypeRequest(request)) {
            wrapper.setValue(new ContentCachingResponseWrapper(response));
        }
        extractIdempotentKeyFromHttpServletRequest(request).ifPresent(idempotentKey -> fineractRequestContextHolder
                .setAttribute(SynchronousCommandProcessingService.IDEMPOTENCY_KEY_ATTRIBUTE, idempotentKey, request));

        filterChain.doFilter(request, wrapper.getValue() != null ? wrapper.getValue() : response);
        Optional<Long> commandId = helper.getCommandId(request);
        boolean isSuccessWithoutStored = commandId.isPresent() && wrapper.getValue() != null && helper.isStoreIdempotencyKey(request)
                && helper.isAllowedContentTypeResponse(response);
        if (isSuccessWithoutStored) {
            helper.storeCommandResult(response.getStatus(), Optional.ofNullable(wrapper.getValue())
                            .map(ContentCachingResponseWrapper::getContentAsByteArray).map(s -> new String(s, StandardCharsets.UTF_8)).orElse(null),
                    commandId.get());
        }
        if (wrapper.getValue() != null) {
            wrapper.getValue().copyBodyToResponse();
        }
    }

    private Optional<String> extractIdempotentKeyFromHttpServletRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(fineractProperties.getIdempotencyKeyHeaderName()));
    }
}