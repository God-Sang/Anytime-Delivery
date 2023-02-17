package com.godsang.anytimedelivery.deliveryArea.service;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    if (deliveryArea.isEmpty()) {
      throw new BusinessLogicException(ExceptionCode.ADDRESS_NOT_EXIST);
    }
    return deliveryArea.get();
  }

  private DeliveryArea createDeliveryArea(String juso) {
    return deliveryAreaRepository.save(new DeliveryArea(juso));
  }
}
