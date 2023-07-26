package com.godsang.anytimedelivery.auth.handler;

import com.godsang.anytimedelivery.auth.utils.AuthErrorResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 비로그인, 세션 만료 handler
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    log.warn("인증 없는 요청: {}", authException.getMessage());

    String message = "Login required.";
    AuthErrorResponseUtils.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, message);
  }
}
