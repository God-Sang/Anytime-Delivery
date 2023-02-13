package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import com.godsang.anytimedelivery.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
public class AddressServiceTest {
  @Autowired
  private AddressService addressService;
  @MockBean
  private UserService userService;
  @MockBean
  private DeliveryAreaService deliveryAreaService;

  @Test
  @DisplayName("저장 테스트")
  void saveTest() {
    //given
    String juso = "서울시 행복구 행복동";
    String detailAddress = "105호";
    Address address = StubData.MockAddress.getMockAddress(juso, detailAddress);
    DeliveryArea deliveryArea = new DeliveryArea(juso);
    User user = StubData.MockUser.getMockEntity();
    given(deliveryAreaService.findExistedDeliveryArea(any())).willReturn(deliveryArea);
    given(userService.findUser(any())).willReturn(user);

    //when & then
    assertThatNoException().isThrownBy(() -> {
      addressService.saveAddress(1L, address, juso);
    });
  }

  @Test
  @DisplayName("캐시 이용한 조회 테스트")
  void retrieveWithCacheTest() {
    //given
    String juso = "서울시 행복구 행복동";
    String detailAddress = "105호";
    Long userId = 9999L;
    DeliveryArea deliveryArea = new DeliveryArea(juso);
    Address address = StubData.MockAddress.getMockAddress(juso, detailAddress);
    address.setDeliveryArea(deliveryArea);
    User user = StubData.MockUser.getMockEntity();
    user.setAddress(address);
    given(userService.findUser(any())).willReturn(user);
    addressService.saveAddress(userId, address, detailAddress); //Cache Evict

    Address retrievedAddress;
    for(int i = 0; i < 10; i++) {
      address = addressService.getAddress(userId);
    }

    verify(userService, times(2)).findUser(any()); //save할 때 한번, 조회할 때 한번

    addressService.saveAddress(userId, address, detailAddress); //Cache Evict as rollback
  }

  @Test
  @DisplayName("주소 저장 안됐을 시 조회 예외")
  void retrieveExceptionTest() {
    //given
    Long userId = 9999L;
    User user = StubData.MockUser.getMockEntity();
    user.setAddress(null);
    given(userService.findUser(any())).willReturn(user);

    //when & then
    assertThatThrownBy(() -> addressService.getAddress(userId))
        .isInstanceOf(BusinessLogicException.class);
  }
}
