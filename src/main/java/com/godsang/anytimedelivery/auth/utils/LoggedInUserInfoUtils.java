package com.godsang.anytimedelivery.auth.utils;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * retrieve user's info from SecurityContext
 */
@Component
public class LoggedInUserInfoUtils {

  /**
   * Retrieve userId from SecurityContext.
   *
   * @return userId or null if a user has not logged in.
   */
  public Long extractUserId() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    try {
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      return userDetails.getUserId();
    } catch (ClassCastException e) {
      return null;
    }
  }
}
