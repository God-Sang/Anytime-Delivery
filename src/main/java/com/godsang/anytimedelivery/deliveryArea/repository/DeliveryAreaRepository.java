package com.godsang.anytimedelivery.deliveryArea.repository;

import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {
  Optional<DeliveryArea> findByJuso(String juso);

  /**
   * User의 id로 DeliveryArea 조회.
   * @param userId
   * @return Optional<DeliveryArea>
   */
  @Query(value = "SELECT da.delivery_area_id, da.juso FROM delivery_area AS da " +
            "INNER JOIN address AS a " +
              "ON da.delivery_area_id = a.delivery_area_id " +
            "INNER JOIN users AS u " +
              "ON a.address_id = u.address_id " +
            "WHERE u.user_id = :userId", nativeQuery = true)
  Optional<DeliveryArea> findUserDeliveryArea(@Param("userId") Long userId);
}
