package com.godsang.anytimedelivery.order.service;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.repository.OrderRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final StoreService storeService;
  private final DeliveryAreaService deliveryAreaService;
  private final PayService payService;

  @Transactional
  public Order request(Order order) {
    Store store = storeService.findStoreById(order.getStore().getStoreId());
    validateStore(store, order.getUser().getUserId());
    payService.pay();
    return orderRepository.save(order);
  }

  @Transactional
  public void cancel(Long orderId, Long userId) {
    Order order = findVerifiedOrder(orderId);
    checkIfUsersOrder(order, userId);
    order.changeStatesToCanceled();
    orderRepository.save(order);
  }

  private void checkIfUsersOrder(Order order, Long userId) {
    if (order.getUser().getUserId() != userId) {
      throw new BusinessLogicException(ExceptionCode.ORDER_NOT_YOURS);
    }
  }

  private Order findVerifiedOrder(Long orderId) {
    Optional<Order> optionalOrder = orderRepository.findById(orderId);
    if (optionalOrder.isEmpty()) {
      throw new BusinessLogicException(ExceptionCode.ORDER_NOT_EXIST);
    }
    return optionalOrder.get();
  }

  @Transactional(readOnly = true)
  private void validateStore(Store store, Long userId) {
    storeService.verifyOpen(store);
    verifyPossibleArea(store, userId);
  }

  @Transactional(readOnly = true)
  public void verifyPossibleArea(Store store, Long userId) {
    boolean isPossible = false;
    DeliveryArea userDeliveryArea = deliveryAreaService.findUserDeliveryArea(userId);
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
