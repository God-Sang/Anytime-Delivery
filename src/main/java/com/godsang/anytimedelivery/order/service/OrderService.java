package com.godsang.anytimedelivery.order.service;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.order.entity.CanceledOrder;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.repository.OrderRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final StoreService storeService;
  private final DeliveryAreaService deliveryAreaService;
  private final PayService payService;

  /**
   * save an order as OrderStatus.WAIT.
   *
   * @param order
   * @return Order
   * @throws BusinessLogicException - 가게가 닫은 경우
   * @throws BusinessLogicException - 배달 불가 지역인 경우
   */
  @Transactional
  public void request(Order order) {
    Store store = storeService.findStoreById(order.getStore().getStoreId());
    storeService.verifyOpen(store);
    deliveryAreaService.verifyPossibleArea(store, order.getUser().getUserId());
    payService.pay();
    orderRepository.save(order);
  }

  /**
   * 고객의 주문 취소.
   *
   * @param orderId
   * @param userId
   * @throws BusinessLogicException - 존재하지 않는 주문인 경우
   * @throws BusinessLogicException - 유저의 주문이 아닌 경우
   * @throws BusinessLogicException - 주문 상태가 WAIT가 아닌 경우
   */

  @Transactional
  public void cancelFromCustomer(Long orderId, Long userId) {
    Order order = findVerifiedOrder(orderId, false);
    checkIfUsersOrder(order, userId);
    cancelOrder(order, new CanceledOrder(order));
  }

  /**
   * 사장의 주문 취소
   *
   * @param orderId
   * @param storeId
   * @param userId
   * @param reason
   * @throws BusinessLogicException - 유저의 가게가 아닌 경우
   * @throws BusinessLogicException - 주문이 존재하지 않는 경우
   * @throws BusinessLogicException - 가게에 요청한 주문이 아닌 경우
   * @throws BusinessLogicException - 주문 상태가 WAIT가 아닌 경우
   */
  @Transactional
  public void cancelFromOwner(Long orderId, Long storeId, Long userId, String reason) {
    storeService.verifyStoreOwner(storeId, userId);
    Order order = findVerifiedOrder(orderId, false);
    verifyOrderBelongToStore(order, storeId);
    cancelOrder(order, new CanceledOrder(reason, order));
  }



  /**
   * 사장의 주문 리스트 조회
   *
   * @param storeId
   * @param userId
   * @param orderStatus
   * @param pageable
   * @return 주문 리스트
   * @throws BusinessLogicException - 유저가 가게의 주인이 아닌 경우
   */
  @Transactional(readOnly = true)
  public List<Order> retrieveOrdersOfStore(Long storeId, Long userId, OrderStatus orderStatus, Pageable pageable) {
    Store store = storeService.verifyStoreOwner(storeId, userId);
    List<Order> orders = orderRepository.findAllByStoreAndStatus(store, orderStatus, pageable);
    return orders;
  }

  /**
   * 사장의 상세 주문 조회
   *
   * @param storeId
   * @param userId
   * @param orderId
   * @return 주문 내역
   * @throws BusinessLogicException - 존재하지 않는 주문인 경우
   */
  @Transactional(readOnly = true)
  public Order retrieveOrderOfStore(Long storeId, Long userId, Long orderId) {
    storeService.verifyStoreOwner(storeId, userId);
    Order order = findVerifiedOrder(orderId, true);
    return order;
  }

  /**
   * 고객의 상세 주문 조회
   *
   * @param orderId
   * @param userId
   * @return 주문 내역
   * @throws BusinessLogicException - 주문이 존재하지 않는 경우
   * @throws BusinessLogicException - 유저의 주문이 아닌 경우
   */
  @Transactional(readOnly = true)
  public Order retrieveOrderOfCustomer(Long orderId, Long userId) {
    Order order = findVerifiedOrder(orderId, true);
    checkIfUsersOrder(order, userId);
    return order;
  }

  /**
   * 유저의 주문 리스트 조회
   *
   * @param userId
   * @param pageable
   * @return 주문 리스트
   */
  public List<Order> retrieveOrdersOfCustomer(Long userId, Pageable pageable) {
    return orderRepository.findAllByUser(new User(userId), pageable);
  }

  /**
   * 주문 상태 변경
   *
   * 주문 상태 변경에는 순서가 있으며, 이를 지키지 않으면 예외를 던집니다.
   * WAIT -> ACCEPTED -> DELIVERED
   *
   * @param storeId
   * @param orderId
   * @param userId
   * @param orderStatus
   * @throws BusinessLogicException - 가게의 주인이 아닌 경우
   * @throws BusinessLogicException - 순서를 지키지 않는 상태 변경인 경우
   * @throws BusinessLogicException - 주문이 존재하지 않는 경우
   */
  @Transactional
  public void changeOrderStatus(Long storeId, Long orderId, Long userId, OrderStatus orderStatus) {
    storeService.verifyStoreOwner(storeId, userId);
    Order order = findVerifiedOrder(orderId, false);
    order.changeStatus(orderStatus);
    orderRepository.save(order);
  }

  /**
   * 주문을 조회하는 경우를 두 가지로 나눠 조회합니다.: 유저에게 전달되는 경우, 아닌 경우
   * 유저에게 전달되는 경우는 Controller의 Mapper에서 Order와 연관된 엔티티를 함께 탐색하기 때문에 fetch join을 사용하고,
   * 그렇지 않은 경우에는 기본적인 조회를 합니다.
   *
   * @param orderId
   * @param isDeliveredToUser
   * @return Order
   */
  private Order findVerifiedOrder(Long orderId, boolean isDeliveredToUser) {
    Optional<Order> optionalOrder;
    if (isDeliveredToUser) {
      optionalOrder = orderRepository.findByOrderId(orderId);
    } else {
      optionalOrder = orderRepository.findById(orderId);
    }
    return optionalOrder.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_EXIST));
  }

  @Transactional
  private void checkIfUsersOrder(Order order, Long userId) {
    if (!order.getUser().getUserId().equals(userId)) {
      throw new BusinessLogicException(ExceptionCode.ORDER_NOT_YOURS);
    }
  }

  @Transactional
  private void cancelOrder(Order order, CanceledOrder canceledOrder) {
    order.changeStatus(OrderStatus.CANCELED);
    order.setCanceledOrder(canceledOrder);
    orderRepository.save(order);
  }

  private void verifyOrderBelongToStore(Order order, Long storeId) {
    if (!order.getStore().getStoreId().equals(storeId)) {
      throw new BusinessLogicException(ExceptionCode.ORDER_NOT_BELONG_TO_STORE);
    }
  }
}
