package com.fineract.mifos.mifos_core.infrastructure.core.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class RequestResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("Received request: [{}], [{}]", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
        log.debug("Sent response: [{}] for [{}], [{}]", response.getStatus(), request.getMethod(), request.getRequestURI());
    }
}
