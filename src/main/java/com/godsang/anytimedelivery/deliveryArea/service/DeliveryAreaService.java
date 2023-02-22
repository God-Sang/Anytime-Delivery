package com.godsang.anytimedelivery.deliveryArea.service;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryAreaService {
  private final DeliveryAreaRepository deliveryAreaRepository;

  /**
   * 등록된 배달 가능 지역이 있는지 찾고, 없으면 새로 생성
   * @Param juso oo시 oo구 oo동
   * @return deliveryArea
   */
  public DeliveryArea findExistedDeliveryArea(String juso) {
    Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findByJuso(juso);
    return deliveryArea.orElseGet(() -> createDeliveryArea(juso));
  }

  public DeliveryArea findUserDeliveryArea(Long userId) {
    Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findUserDeliveryArea(userId);
    return deliveryArea.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADDRESS_NOT_EXIST));
  }

  private DeliveryArea createDeliveryArea(String juso) {
    return deliveryAreaRepository.save(new DeliveryArea(juso));
  }

  /**
   * 배달 가능 지역인지 확인
   *
   * @param store
   * @param userId
   */
  @Transactional(readOnly = true)
  public void verifyPossibleArea(Store store, Long userId) {
    boolean isPossible = false;
    DeliveryArea userDeliveryArea = findUserDeliveryArea(userId);
    for (DeliveryAreaStore deliveryAreaStore : store.getDeliveryAreaStores()) {
      Long storeDeliveryAreaId = deliveryAreaStore.getDeliveryArea().getDeliveryAreaId();
      if (storeDeliveryAreaId == userDeliveryArea.getDeliveryAreaId()) {
        isPossible = true;
        break;
      }
    }
    if (!isPossible) {
      throw new BusinessLogicException(ExceptionCode.NOT_IN_DELIVERY_AREA);
    }
  }
}
