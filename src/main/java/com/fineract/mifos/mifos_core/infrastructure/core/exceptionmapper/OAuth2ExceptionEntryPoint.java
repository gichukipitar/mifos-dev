package com.fineract.mifos.mifos_core.infrastructure.core.exceptionmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fineract.mifos.mifos_core.infrastructure.core.data.ApiGlobalErrorResponse;
import com.fineract.mifos.mifos_core.infrastructure.core.exception.ErrorHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class OAuth2ExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws ServletException {
        log.warn("Exception occurred", ErrorHandler.findMostSpecificException(exception));
        ApiGlobalErrorResponse errorResponse = ApiGlobalErrorResponse.unAuthenticated();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), errorResponse);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
