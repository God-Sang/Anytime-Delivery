package com.godsang.anytimedelivery.auth.handler;

import com.godsang.anytimedelivery.auth.utils.AuthErrorResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 실패 handler
 */
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.error("# 로그인 실패: {}", exception.getMessage());

    String message = "Login failed.";
    AuthErrorResponseUtils.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, message);
  }
}
