package com.godsang.anytimedelivery.order.repository;

import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findAllByStore(Store store);

  @Query("SELECT o FROM ORDERS o JOIN FETCH o.user u JOIN FETCH u.address a WHERE o.store = :store AND o.status = :status")
  List<Order> findAllByStoreAndStatus(Store store, OrderStatus status, Pageable pageable);
}
