package com.godsang.anytimedelivery.order.entity;

import com.godsang.anytimedelivery.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OrderMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderMenuId;
  @Column(nullable = false)
  private Integer amount;
  @ManyToOne(optional = false)
  private Order order;
  @ManyToOne(optional = false)
  private Menu menu;
  @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderGroup> orderGroups = new ArrayList<>();

  @Builder
  public OrderMenu(Integer amount, Order order, Menu menu) {
    this.amount = amount;
    this.order = order;
    this.menu = menu;
  }

  public void addOrderGroup(OrderGroup orderGroup) {
    orderGroups.add(orderGroup);
  }
}
