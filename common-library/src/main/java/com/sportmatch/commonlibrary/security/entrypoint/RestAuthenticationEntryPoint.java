package com.sportmatch.commonlibrary.security.entrypoint;

import com.sportmatch.commonlibrary.constants.MessageConstant;
import com.sportmatch.commonlibrary.dto.ErrorResponse;
import com.sportmatch.commonlibrary.error.ErrorMessageResolver;
import com.sportmatch.commonlibrary.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e)
            throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        ErrorResponse error = ErrorMessageResolver.getExceptionError(MessageConstant.UNAUTHORIZED);
        httpServletResponse
                .getWriter()
                .write(Objects.requireNonNull(JsonUtils.convertJsonToString(error)));
    }
}