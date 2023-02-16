package com.godsang.anytimedelivery.order.repository;

import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findAllByStore(Store store);
}
