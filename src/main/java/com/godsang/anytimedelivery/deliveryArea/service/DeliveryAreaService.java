package com.godsang.anytimedelivery.deliveryArea.service;

import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
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
  @Transactional
  public DeliveryArea findExistedDeliveryArea(String juso) {
    Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findByJuso(juso);
    return deliveryArea.orElseGet(() -> createDeliveryArea(juso));
  }

  private DeliveryArea createDeliveryArea(String juso) {
    return deliveryAreaRepository.save(new DeliveryArea(juso));
  }
}
