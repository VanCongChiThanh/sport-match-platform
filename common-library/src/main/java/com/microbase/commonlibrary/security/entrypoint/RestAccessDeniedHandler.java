package com.microbase.commonlibrary.security.entrypoint;

import com.microbase.commonlibrary.constants.MessageConstant;
import com.microbase.commonlibrary.dto.ErrorResponse;
import com.microbase.commonlibrary.error.ErrorMessageResolver;
import com.microbase.commonlibrary.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse resolvedError = ErrorMessageResolver.getExceptionError(MessageConstant.FORBIDDEN_ERROR);
        ErrorResponse error = ErrorResponse.of(
                resolvedError.code(),
                resolvedError.message(),
                HttpServletResponse.SC_FORBIDDEN,
                request.getRequestURI()
        );
        response.getWriter().write(Objects.requireNonNull(JsonUtils.convertJsonToString(error)));
    }
}