package com.godsang.anytimedelivery.auth.details;

import com.godsang.anytimedelivery.auth.utils.UserAuthorityUtils;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserAuthorityUtils userAuthorityUtils;
  private final UserRepository userRepository;

  /**
   * login 시 parameter로 받은 username이 DB에 존재하는 지 확인
   * unique인 email로 사용자 확인
   *
   * @Return UserDetailsImpl
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    return new UserDetailsImpl(user, userAuthorityUtils.createAuthorities(user.getRole()));
  }
}
