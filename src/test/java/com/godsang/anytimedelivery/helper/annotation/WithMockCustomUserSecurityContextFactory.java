package com.godsang.anytimedelivery.helper.annotation;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

/**
 * custom security context for test
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    Role role = customUser.role();
    User user = StubData.MockUser.getMockEntity(role);
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role.toString());
    UserDetailsImpl principal = new UserDetailsImpl(user, authorities);

    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "password", authorities);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    return context;
  }
}
