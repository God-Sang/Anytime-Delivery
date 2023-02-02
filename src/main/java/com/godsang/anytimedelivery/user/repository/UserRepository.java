package com.godsang.anytimedelivery.user.repository;

import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);

  boolean existsByNickName(String nickName);

  Optional<User> findByEmail(String email);
}
