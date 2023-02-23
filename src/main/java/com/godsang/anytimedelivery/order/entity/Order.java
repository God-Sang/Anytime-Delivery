package com.godsang.anytimedelivery.order.entity;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.common.audit.BaseEntity;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ORDERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.WAIT;
  private String request;
  @Column(nullable = false)
  private int foodTotalPrice;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;
  @BatchSize(size = 500)
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "order_id")
  private List<OrderMenu> orderMenus = new ArrayList<>();
  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private CanceledOrder canceledOrder;

  @Builder
  private Order(Long orderId, String request, int foodTotalPrice, User user, Store store) {
    this.orderId = orderId;
    this.request = request;
    this.user = user;
    this.store = store;
    this.foodTotalPrice = foodTotalPrice;
  }

  public void addOrderMenu(OrderMenu orderMenu) {
    orderMenus.add(orderMenu);
  }

  public void setCanceledOrder(CanceledOrder canceledOrder) {
    this.canceledOrder = canceledOrder;
  }

  public void changeStatus(OrderStatus orderStatus) {
    switch (orderStatus) {
      case ACCEPTED:
        changeStatusToAccepted();
        break;
      case DELIVERED:
        changeStatusToDelivered();
        break;
      case CANCELED:
        changeStatusToCanceled();
        break;
      default:
        throw new BusinessLogicException(ExceptionCode.UNABLE_TO_CHANGE_TO_WAIT);
    }
  }

  private void changeStatusToAccepted() {
    if (this.status != OrderStatus.WAIT) {
      throw new BusinessLogicException(ExceptionCode.INVALID_ORDER_STATES_CHANGE);
    }
    this.status = OrderStatus.ACCEPTED;
  }

  private void changeStatusToDelivered() {
    if (this.status != OrderStatus.ACCEPTED) {
      throw new BusinessLogicException(ExceptionCode.INVALID_ORDER_STATES_CHANGE);
    }
    this.status = OrderStatus.DELIVERED;
  }

  private void changeStatusToCanceled() {
    if (this.status != OrderStatus.WAIT) {
      throw new BusinessLogicException(ExceptionCode.INVALID_ORDER_STATES_CHANGE);
    }
    this.status = OrderStatus.CANCELED;
  }
}
