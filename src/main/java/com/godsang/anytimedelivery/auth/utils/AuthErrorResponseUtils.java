package com.godsang.anytimedelivery.auth.utils;

import com.godsang.anytimedelivery.common.Exception.ErrorResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication, Authority 관련 에러 발생 시 response 포맷
 */
public class AuthErrorResponseUtils {
  public static void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
    Gson gson = new Gson();

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(status.value());
    response.getWriter().write(gson.toJson(ErrorResponse.of(status, message)));
  }
}
