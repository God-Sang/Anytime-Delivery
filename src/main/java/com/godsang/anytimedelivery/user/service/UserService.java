package com.godsang.anytimedelivery.user.service;

import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(User user, boolean isCustomer) {
        verifyUserEmailExists(user.getEmail());
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
//        user.setRole(); TODO role 세팅
        userRepository.save(user);
    }

    private void verifyUserEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException(); // TODO 예외 커스텀
        }
    }
}