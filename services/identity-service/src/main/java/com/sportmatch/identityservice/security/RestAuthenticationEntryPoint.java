package com.sportmatch.identityservice.security;


import com.sportmatch.identityservice.common.util.CommonFunction;
import com.sportmatch.identityservice.common.constants.MessageConstant;
import com.sportmatch.identityservice.common.response.ResponseDataAPI;
import com.sportmatch.identityservice.common.response.ErrorResponse;
import com.sportmatch.identityservice.common.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


import java.io.IOException;
import java.util.Objects;

@Slf4j
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
        ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.UNAUTHORIZED);
        ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
        LogUtils.error(
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURL().toString(),
                e.getMessage());
        httpServletResponse
                .getWriter()
                .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    }
}