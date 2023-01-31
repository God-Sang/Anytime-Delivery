package com.godsang.anytimedelivery.auth.handler;

import com.godsang.anytimedelivery.auth.utils.AuthErrorResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 접근 권한이 없는 요청 handler
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("권한 없는 요청: {}", accessDeniedException.getMessage());

        String message = "UnAuthorized Request.";
        AuthErrorResponseUtils.sendErrorResponse(response, HttpStatus.FORBIDDEN, message);
    }
}
