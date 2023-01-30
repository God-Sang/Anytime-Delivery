package com.godsang.anytimedelivery.auth.service;

import com.godsang.anytimedelivery.auth.utils.UserAuthorityUtils;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserAuthorityUtils userAuthorityUtils;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new UserDetailsImpl(user);
    }

    private final class UserDetailsImpl implements UserDetails {
        private final User user;

        private UserDetailsImpl(User user) {
            this.user = user;
        }

        /**
         * 해당 user의 권한 목록
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return userAuthorityUtils.createAuthorities(user.getRole());
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
}
