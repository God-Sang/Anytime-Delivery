package com.godsang.anytimedelivery.order.repository;

import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
  /**
   * 가게와 주문상태를 필터로 주문 리스트 조회.
   * 함께 조회되는 연관 엔티티를 fetch join으로 한번에 조회합니다.
   *
   * @param store
   * @param status
   * @param pageable
   * @return 주문 리스트
   */
  @Query("SELECT o FROM ORDERS o JOIN FETCH o.user u JOIN FETCH u.address a WHERE o.store = :store AND o.status = :status")
  List<Order> findAllByStoreAndStatus(Store store, OrderStatus status, Pageable pageable);

  /**
   * 주문 상세 조회를 위한 주문 아이디를 가지고 주문 조회.
   *
   * 연관된 엔티티를 모두 조회하기 때문에 N+1문제를 방지하기 위하여,
   * 1:1 N:1 관계의 엔티티는 join fetch로 한번에 조회하고
   * 1:N 관계의 엔티티는 @BatchSize를 통해 in 절로 조회했습니다.
   * 일반 주문 조회는 findById(Long id)로 수행합니다.
   * @param orderId
   * @return 상세 주문
   */
  @Query("SELECT distinct o FROM ORDERS o JOIN FETCH o.user u JOIN FETCH u.address a JOIN FETCH o.store s WHERE o.orderId = :orderId")
  Optional<Order> findByOrderId(Long orderId);

  /**
   * 유저가 주문 목록을 조회하기 위해 호출됩니다.
   * 함께 조회될 엔티티는 join fetch로 한번에 조회했습니다.
   * @param user
   * @param pageable
   * @return 주문 리스트
   */
  @Query("SELECT distinct o FROM ORDERS o JOIN FETCH o.user u JOIN FETCH u.address a JOIN FETCH o.store s WHERE o.user = :user")
  List<Order> findAllByUser(User user, Pageable pageable);
}
