package com.microbase.commonlibrary.security.entrypoint;

import com.microbase.commonlibrary.constants.MessageConstant;
import com.microbase.commonlibrary.dto.ErrorResponse;
import com.microbase.commonlibrary.error.ErrorMessageResolver;
import com.microbase.commonlibrary.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse resolvedError = ErrorMessageResolver.getExceptionError(MessageConstant.UNAUTHORIZED);
        ErrorResponse error = ErrorResponse.of(
                resolvedError.code(),
                resolvedError.message(),
                HttpServletResponse.SC_UNAUTHORIZED,
                request.getRequestURI()
        );
        response.getWriter().write(Objects.requireNonNull(JsonUtils.convertJsonToString(error)));
    }
}