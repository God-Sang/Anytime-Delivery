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
  @Column
  private Integer deliveryFee;
  @Column
  private Short deliveryTime;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "order_id")
  @BatchSize(size = 10)
  private List<OrderMenu> orderMenus = new ArrayList<>();

  @Builder
  private Order(Long orderId, OrderStatus status, String request, Integer deliveryFee, Short deliveryTime,
                User user, Store store) {
    this.orderId = orderId;
    this.status = status;
    this.request = request;
    this.deliveryFee = deliveryFee;
    this.deliveryTime = deliveryTime;
    this.user = user;
    this.store = store;
  }

  public void addOrderMenu(OrderMenu orderMenu) {
    orderMenus.add(orderMenu);
  }

  public void addFoodTotalPrice(int price) {
    this.foodTotalPrice += price;
  }

  public void changeStateToAccepted() {
    if (this.status != OrderStatus.WAIT) {
      throw new BusinessLogicException(ExceptionCode.INVALID_ORDER_STATES_CHANGE);
    }
    this.status = OrderStatus.ACCEPTED;
  }

  public void changeStatesToDelivered() {
    if (this.status != OrderStatus.ACCEPTED) {
      throw new BusinessLogicException(ExceptionCode.INVALID_ORDER_STATES_CHANGE);
    }
    this.status = OrderStatus.DELIVERED;
  }

  public void changeStatesToCanceled() {
    if (this.status == OrderStatus.DELIVERED) {
      throw new BusinessLogicException(ExceptionCode.INVALID_ORDER_STATES_CHANGE);
    }
    this.status = OrderStatus.CANCELED;
  }
}
