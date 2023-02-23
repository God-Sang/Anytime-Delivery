package com.godsang.anytimedelivery.deliveryArea;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeliveryAreaServiceTest {
  @Autowired
  private DeliveryAreaService deliveryAreaService;
  @MockBean
  private DeliveryAreaRepository deliveryAreaRepository;

  @Test
  @DisplayName("가게의 배달가능 지역에 유저의 배달지역이 포함되지 않는 경우 에러 발생")
  void findUserDeliveryAreaTest() {
    //given
    Long userId = 1L;
    DeliveryArea userDeliveryArea = mock(DeliveryArea.class);
    when(userDeliveryArea.getDeliveryAreaId()).thenReturn(1111L);
    List<DeliveryAreaStore> deliveryAreaStores = new ArrayList<>();
    for (Long i = 1L; i <= 5L; i++) {
      DeliveryArea storeDeliveryArea = mock(DeliveryArea.class);
      when(storeDeliveryArea.getDeliveryAreaId()).thenReturn(i);
      DeliveryAreaStore deliveryAreaStore = new DeliveryAreaStore(null, storeDeliveryArea);
      deliveryAreaStores.add(deliveryAreaStore);
    }
    Store store = mock(Store.class);
    when(store.getDeliveryAreaStores()).thenReturn(deliveryAreaStores);
    given(deliveryAreaRepository.findUserDeliveryArea(anyLong())).willReturn(Optional.of(userDeliveryArea));

    //when&then
    assertThrows(BusinessLogicException.class,
        () -> deliveryAreaService.verifyPossibleArea(store, userId));
  }
}
