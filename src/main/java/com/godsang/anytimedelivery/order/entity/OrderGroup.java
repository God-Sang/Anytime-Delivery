package com.godsang.anytimedelivery.order.entity;

import com.godsang.anytimedelivery.menu.entity.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderGroupId;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_menu_id")
  private OrderMenu orderMenu;
  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;
  @OneToMany(mappedBy = "orderGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<OrderOption> orderOptions = new ArrayList<>();

  @Builder
  private OrderGroup(OrderMenu orderMenu, Group group) {
    this.orderMenu = orderMenu;
    this.group = group;
  }

  public void addOrderOption(OrderOption orderOption) {
    orderOptions.add(orderOption);
  }
}
