package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AddressRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("유저 - 주소 매핑 테스트")
  void saveAndRetrieveTest() {
    //given
    User user = userRepository.save(StubData.MockUser.getMockEntity(
        2L, "email", "1q2w3e4r!", "010", "nickName", Role.ROLE_CUSTOMER));

    Long userId = 1L;
    String addr = "서울시 행복구 행복동";
    Address address = Address.builder()
        .address(addr)
        .detailAddress("10층 페이팀")
        .build();

    //when
    User foundUser = userRepository.findById(user.getUserId()).get();
    foundUser.setAddress(address);
    userRepository.save(foundUser);

    String retrievedAddress = userRepository.findById(userId).get().getAddress().getAddress();

    // then
    assertThat(retrievedAddress).isEqualTo(addr);
  }
}
