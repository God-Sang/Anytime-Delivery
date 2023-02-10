package com.godsang.anytimedelivery.auth.utils;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserInfoUtils {
  public Long extractUserId() {
    Map<?, ?> claims;
    try {
      SecurityContext context = SecurityContextHolder.getContext();
      Authentication authentication = context.getAuthentication();
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      return userDetails.getUserId();
    } catch (Exception e) {
      throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
    }
  }
}
