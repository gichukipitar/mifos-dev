package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import com.fineract.mifos.mifos_core.infrastructure.core.config.FineractProperties;
import com.fineract.mifos.mifos_core.infrastructure.core.service.MDCWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CorrelationHeaderFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_KEY = "correlationId";

    private final FineractProperties fineractProperties;
    private final MDCWrapper mdcWrapper;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        FineractProperties.FineractCorrelationProperties correlationProperties = fineractProperties.getCorrelation();
        if (correlationProperties.isEnabled()) {
            handleCorrelations(request, response, filterChain, correlationProperties);
        } else {
            filterChain.doFilter(request, response);
        }

    }

    private void handleCorrelations(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                    FineractProperties.FineractCorrelationProperties correlationProperties) throws IOException, ServletException {
        try {
            String correlationHeaderName = correlationProperties.getHeaderName();
            String correlationId = request.getHeader(correlationHeaderName);
            if (StringUtils.isNotBlank(correlationId)) {
                String escapedCorrelationId = LogParameterEscapeUtil.escapeLogMDCParameter(correlationId);
                log.debug("Found correlationId in header : {}", escapedCorrelationId);
                mdcWrapper.put(CORRELATION_ID_KEY, escapedCorrelationId);
            }
            filterChain.doFilter(request, response);
        } finally {
            mdcWrapper.remove(CORRELATION_ID_KEY);
        }
    }

    @Override
    protected boolean isAsyncDispatch(final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

}
