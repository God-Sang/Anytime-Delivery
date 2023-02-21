package com.godsang.anytimedelivery.order.entity;

import com.godsang.anytimedelivery.menu.entity.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderMenuId;
  @Column(nullable = false)
  private int amount;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;
  @ManyToOne(optional = false)
  @JoinColumn(name = "menu_id")
  private Menu menu;
  @BatchSize(size = 10)
  @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<OrderGroup> orderGroups = new ArrayList<>();

  @Builder
  private OrderMenu(Integer amount, Order order, Menu menu) {
    this.amount = amount;
    this.order = order;
    this.menu = menu;
  }

  public void addOrderGroup(OrderGroup orderGroup) {
    orderGroups.add(orderGroup);
  }
}
