package com.godsang.anytimedelivery.user.repository;

import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
