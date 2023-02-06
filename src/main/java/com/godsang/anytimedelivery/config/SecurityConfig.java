package com.godsang.anytimedelivery.config;

import com.godsang.anytimedelivery.auth.handler.*;
import com.godsang.anytimedelivery.user.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
  private final String ADMIN = Role.ROLE_ADMIN.getName();
  private final String CUSTOMER = Role.ROLE_CUSTOMER.getName();
  private final String OWNER = Role.ROLE_OWNER.getName();

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .headers().frameOptions().sameOrigin()
        .and()
        .csrf().disable()
        .cors(Customizer.withDefaults()) // cors
        .formLogin() // 로그인 설정
        .loginProcessingUrl("/users/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .successHandler(new CustomAuthenticationSuccessHandler())
        .failureHandler(new CustomAuthenticationFailureHandler())
        .and()
        .logout() // 로그아웃 설정
        .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
        .invalidateHttpSession(true) // 세션 초기화
        .and()
        .exceptionHandling() // 예외 처리 handler
        .accessDeniedHandler(new CustomAccessDeniedHandler())
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .authorizeHttpRequests() // TODO 권한 path 분기
        .antMatchers(HttpMethod.POST, "/customer/**").hasRole(CUSTOMER)
        .antMatchers(HttpMethod.PATCH, "/customer/**").hasRole(CUSTOMER)
        .antMatchers(HttpMethod.DELETE, "/customer/**").hasRole(CUSTOMER)
        .antMatchers(HttpMethod.POST, "/owner/**").hasRole(OWNER)
        .antMatchers(HttpMethod.GET, "/owner/**").hasRole(OWNER)
        .antMatchers(HttpMethod.PATCH, "/owner/**").hasRole(OWNER)
        .antMatchers(HttpMethod.DELETE, "/owner/**").hasRole(OWNER)
        .antMatchers(HttpMethod.POST, "/categories/**").hasRole(ADMIN)
        .antMatchers(HttpMethod.PATCH, "/categories/**").hasRole(ADMIN)
        .antMatchers(HttpMethod.DELETE, "/categories/**").hasRole(ADMIN)
        .anyRequest().permitAll();
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
