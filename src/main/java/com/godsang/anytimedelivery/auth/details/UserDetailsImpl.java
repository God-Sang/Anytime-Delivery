package com.godsang.anytimedelivery.auth.details;

import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
  private final User user;
  private final Long userId;
  private final List<GrantedAuthority> roles;

  public UserDetailsImpl(User user, List<GrantedAuthority> roles) {
    this.user = user;
    this.roles = roles;
    this.userId = user.getUserId();
  }

  public Long getUserId() {
    return this.userId;
  }

  /**
   * 해당 user의 권한 목록
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  /**
   * 패스워드
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  /**
   * unique인 이메일
   */
  @Override
  public String getUsername() {
    return user.getEmail();
  }

  /**
   * 계정 만료 여부
   * true: 만료 되지 않음
   * false: 만료
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 계정 잠김 여부
   * true: 잠기지 않음
   * false: 잠김
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 비밀번호 만료 여부
   * true: 만료 되지 않음
   * false: 만료
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 사용자 홣성화 여부
   * true: 활성화
   * false: 비활성화
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}
