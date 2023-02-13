package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import com.godsang.anytimedelivery.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressServiceTest {
  @Autowired
  private AddressService addressService;
  @Autowired
  private UserRepository userRepository;
  @BeforeAll
  void init() {
    User user = StubData.MockUser.getMockEntity(1L, "abcd@naver.com", "abcd1234!@#",
        "010-1111-1111", "nick", Role.ROLE_CUSTOMER);
    userRepository.save(user);
  }
  @Test
  @DisplayName("저장 테스트")
  @Transactional
  void saveTest() {
    //given
    Long userId = 1L;
    String address = "서울시 행복구 행복동";
    String detailAddress = "104동 902호";
    Address userAddress = StubData.MockAddress.getMockAddress(address, detailAddress);
    //when
    addressService.saveAddress(userId, userAddress);
    String savedAddress = userRepository.findById(userId).get().getAddress().getAddress();
    //then
    assertThat(savedAddress)
        .isEqualTo(address);
  }

}
