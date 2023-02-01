package com.godsang.anytimedelivery.user.service;

import com.godsang.anytimedelivery.auth.utils.UserAuthorityUtils;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
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
    private final UserAuthorityUtils userAuthorityUtils;

    /**
     * 패스워드 인코딩, role 생성 후 DB 저장
     * @Return User
     */
    @Transactional
    public User createUser(User user, String role) {
        verifyDuplicatedSignupInfo(user.getEmail(), user.getPhone(), user.getNickName());
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(userAuthorityUtils.createRoles(role, user.getEmail()));
        return userRepository.save(user);
    }

    /**
     * 회원가입 시 unique 필드 중복 확인
     */
    private void verifyDuplicatedSignupInfo(String email, String phone, String nickName) {
        verifyUserEmailExists(email);
        verifyUserPhoneExists(phone);
        verifyUserNickNameExists(nickName);
    }

    /**
     * 재사용을 위해 분리
     * 이메일 중복 확인
     * @throws BusinessLogicException when same email found.
     */
    private void verifyUserEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
        }
    }

    /**
     * 핸드폰 번호 중복 확인
     * @throws BusinessLogicException when same phone number found.
     */
    private void verifyUserPhoneExists(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new BusinessLogicException(ExceptionCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    /**
     * 닉네임 중복 확인
     * @throws BusinessLogicException when same nickname found.
     */
    private void verifyUserNickNameExists(String nickName) {
        if (userRepository.existsByNickName(nickName)) {
            throw new BusinessLogicException(ExceptionCode.NICKNAME_ALREADY_EXISTS);
        }
    }
}
