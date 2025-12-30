package com.sportmatch.identityservice.token;


import com.sportmatch.identityservice.common.util.CommonFunction;
import com.sportmatch.identityservice.common.constant.MessageConstant;
import com.sportmatch.identityservice.entity.UserPrincipal;
import com.sportmatch.identityservice.exception.ForbiddenException;
import com.sportmatch.identityservice.common.response.ResponseDataAPI;
import com.sportmatch.identityservice.common.response.ErrorResponse;
import com.sportmatch.identityservice.common.util.LogUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Objects;
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException {
    try {
      String jwt = getJwtFromRequest(request);

      if (StringUtils.hasText(jwt)) {
        UserDetails userDetails = UserPrincipal.create(tokenProvider.getUserFromToken(jwt));
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
    } catch (ForbiddenException ex) {
      LogUtils.error(request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse error = CommonFunction.getExceptionError(ex.getMessage());
      ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
      response
          .getWriter()
          .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    } catch (Exception ex) {
      LogUtils.error(request.getMethod(), request.getRequestURL().toString(), ex.getMessage());
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.INTERNAL_SERVER_ERROR);
      ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
      response
          .getWriter()
          .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}