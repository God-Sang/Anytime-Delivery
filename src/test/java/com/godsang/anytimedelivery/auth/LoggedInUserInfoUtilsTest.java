package com.godsang.anytimedelivery.auth;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.auth.utils.UserAuthorityUtils;
import com.godsang.anytimedelivery.auth.utils.LoggedInUserInfoUtils;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoggedInUserInfoUtilsTest {
  @Autowired
  private LoggedInUserInfoUtils loggedInUserInfoUtils;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserAuthorityUtils userAuthorityUtils;
  private Long userId = 2L;
  private String email = "abcd@naver.com";
  private String password = "abcd1234";


  @Test
  void extractUserIdTest() throws Exception {
    //given
    User user = userRepository.save(StubData.MockUser.getMockEntity(
        userId, email, password, "010-1234-1234", "nick123", Role.ROLE_CUSTOMER));
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        new UserDetailsImpl(user, userAuthorityUtils.createAuthorities(user.getRole())),
        null, userAuthorityUtils.createAuthorities(user.getRole()));
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);
    //when
    Long userId = loggedInUserInfoUtils.extractUserId();
    //then
    assertThat(userId)
        .isEqualTo(userId);
  }
}
