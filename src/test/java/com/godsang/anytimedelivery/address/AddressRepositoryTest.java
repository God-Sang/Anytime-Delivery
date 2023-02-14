package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AddressRepositoryTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private DeliveryAreaRepository deliveryAreaRepository;

  @Test
  @Transactional
  @DisplayName("유저 - 주소 - 배달지역 매핑 테스트")
  void saveAndRetrieveTest() {
    //given
    User user = userRepository.save(StubData.MockUser.getMockEntity(
        2L, "email", "010", "nickName"));

    Long userId = 1L;
    String addr = "서울시 행복구 행복동";
    Address address = Address.builder()
        .address(addr)
        .detailAddress("10층 페이팀")
        .build();

    DeliveryArea deliveryArea = deliveryAreaRepository.save(new DeliveryArea("서울시 행복구 행복동"));

    //when
    User foundUser = userRepository.findById(user.getUserId()).get();
    address.setDeliveryArea(deliveryArea);
    foundUser.setAddress(address);

    userRepository.save(foundUser);

    User retrievedUser = userRepository.findById(userId).get();
    Address retrievedAddress = retrievedUser.getAddress();
    String retrievedDeliveryArea = retrievedAddress.getDeliveryArea().getJuso();

    // then
    assertThat(retrievedAddress.getAddress()).isEqualTo(addr);
    assertThat(retrievedDeliveryArea).isEqualTo(addr);
  }


}
