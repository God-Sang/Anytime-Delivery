package com.godsang.anytimedelivery.deliveryArea.repository;

import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {
  Optional<DeliveryArea> findByJuso(String juso);
}
