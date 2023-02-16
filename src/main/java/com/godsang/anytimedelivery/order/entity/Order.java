package com.godsang.anytimedelivery.order.entity;

import com.godsang.anytimedelivery.common.audit.BaseEntity;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ORDERS")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;
  @Enumerated(EnumType.STRING)
  private OrderStatus status;
  private String request;
  @Column(nullable = false)
  private int foodTotalPrice;
  @Column
  private Integer deliveryFee;
  @Column
  private Short deliveryTime;
  @ManyToOne(optional = false)
  private User user;
  @ManyToOne(optional = false)
  private Store store;
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderMenu> orderMenus = new ArrayList<>();

  @Builder
  public Order(OrderStatus status, String request, Integer deliveryFee, Short deliveryTime,
               User user, Store store) {
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
}
