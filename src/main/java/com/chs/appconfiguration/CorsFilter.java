package com.chs.appconfiguration;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends GenericFilterBean {

    private static final String ALLOWED_HEADERS = String.join(",", Arrays.asList(
            "*",
            "ckCsrfToken",
            "accept",
            "authorization",
            "client_id",
            "client_secret",
            "content-type",
            "grant_type",
            "origin",
            "X-CSRF-TOKEN",
            "otp-token"));

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(final HttpServletRequest request,
                          final HttpServletResponse response,
                          final FilterChain chain) throws IOException, ServletException {
        // Always add the origins header
        response.setHeader("Access-Control-Allow-Origin", "*");
        if (isPreflightRequest(request)) {
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "86400");
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isPreflightRequest(final HttpServletRequest request) {
        return isOptions(request)
                && (hasHeader(request, "Origin") || hasHeader(request, "Access-Control-Request-Method"));
    }

    private boolean isOptions(final HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    private boolean hasHeader(final HttpServletRequest request,
                              final String name) {
        final var value = request.getHeader(name);
        return value != null && !value.isBlank();
    }
}