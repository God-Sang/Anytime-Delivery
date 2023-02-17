package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressRepositoryTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private DeliveryAreaRepository deliveryAreaRepository;
  private Long userId;
  private String addr = "서울시 행복구 행복동";
  @BeforeAll
  void init() {
    User user = userRepository.save(StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER));
    DeliveryArea deliveryArea = deliveryAreaRepository.save(new DeliveryArea(addr));
    Address address = Address.builder()
        .address("서울시 행복구 행복동")
        .detailAddress("10층 페이팀")
        .build();
    address.setDeliveryArea(deliveryArea);
    user.setAddress(address);
    userRepository.save(user);
    userId = user.getUserId();
  }

  @Test
  @DisplayName("유저 - 주소 - 배달지역 매핑 테스트")
  void saveAndRetrieveTest() {
    User retrievedUser = userRepository.findById(userId).get();
    Address retrievedAddress = retrievedUser.getAddress();
    String retrievedDeliveryArea = retrievedAddress.getDeliveryArea().getJuso();

    // then
    assertThat(retrievedAddress.getAddressId()).isNotNull();
    assertThat(retrievedAddress.getAddress()).isEqualTo(addr);
    assertThat(retrievedDeliveryArea).isEqualTo(addr);
  }
  @Test
  void findUserDeliveryAreaTest() {
    DeliveryArea deliveryArea = deliveryAreaRepository.findUserDeliveryArea(userId).get();

    assertThat(deliveryArea).isNotNull();
  }


}
