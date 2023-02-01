package com.godsang.anytimedelivery.user;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @DataJpaTest의 embedded DB는 H2이다.
 * 현재 MySQL을 사용하고 있으므로 @AutoConfigureTestDatabase 어노테이션을 사용하여 embedded DB로 대체되는 것을 해제한다.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void saveEntity() {
        User user = StubData.MockUser.getMockEntity(1L, "anytime@email.com", "1q2w3e4r@", "010-1234-5678", "애니타임", Role.ROLE_CUSTOMER);
        userRepository.save(user);
    }

    @ParameterizedTest
    @CsvSource(value = {"anytime@email.com:true", "abcd@gmail.com:false"}, delimiter = ':')
    @DisplayName("이메일의 중복여부 체크")
    void verifyDuplicatedEmail(String signupEmail, boolean is_duplicated) {
        assertThat(userRepository.existsByEmail(signupEmail)).isEqualTo(is_duplicated);
    }

    @ParameterizedTest
    @CsvSource(value = {"010-1234-5678:true", "010-1111-2222:false"}, delimiter = ':')
    @DisplayName("핸드폰 번호의 중복여부 체크")
    void verifyDuplicatedPhone(String signupPhone, boolean is_duplicated) {
        assertThat(userRepository.existsByPhone(signupPhone)).isEqualTo(is_duplicated);
    }

    @ParameterizedTest
    @CsvSource(value = {"애니타임:true", "hypeboy:false"}, delimiter = ':')
    @DisplayName("닉네임의 중복여부 체크")
    void verifyDuplicatedNickName(String signupNickName, boolean is_duplicated) {
        assertThat(userRepository.existsByNickName(signupNickName)).isEqualTo(is_duplicated);
    }

    @ParameterizedTest
    @CsvSource(value = {"abcd@gmail.com:true", "newjeans@email.com:true", "anytime@email.com:false"}, delimiter = ':')
    @DisplayName("DB에서 이메일로 User를 찾는다. 없을 경우 빈 객체를 반환한다.")
    void findEmail(String email, boolean is_empty) {
        assertThat(userRepository.findByEmail(email).isEmpty()).isEqualTo(is_empty);
    }

    @ParameterizedTest
    @CsvSource(value = {"anytime@email.com:민지:010-1111-1111", "abcd@email.com:애니타임:010-1111-2222", "abcd@email.com:민지:010-1234-5678"}, delimiter = ':')
    @DisplayName("unique 필드가 중복된 User를 저장하면 에러 발생")
    void uniqueTest(String email, String nickName, String phone) {
        User duplicatedUser = StubData.MockUser.getMockEntity(2L, email, "1q2w3e4r!", phone, nickName, Role.ROLE_OWNER);
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(duplicatedUser));
    }

    @ParameterizedTest
    @CsvSource(value = {"everytime@email.com:민지:010-1111-1111", "abcd@email.com:다니엘:010-1111-2222", "newjeans@email.com:하니:010-1234-1234"}, delimiter = ':')
    @DisplayName("중복되지 않은 User 저장")
    void saveTest(String email, String nickName, String phone) {
        User newUser = StubData.MockUser.getMockEntity(2L, email, "1q2w3e4r!", phone, nickName, Role.ROLE_OWNER);
        assertDoesNotThrow(() -> userRepository.save(newUser));

        List<User> findUsers = userRepository.findAll();
        assertThat(findUsers.size()).isEqualTo(2);
    }
}
