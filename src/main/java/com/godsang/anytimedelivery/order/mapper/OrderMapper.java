package com.godsang.anytimedelivery.order.mapper;

import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderGroup;
import com.godsang.anytimedelivery.order.entity.OrderMenu;
import com.godsang.anytimedelivery.order.entity.OrderOption;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.entity.User;

/**
 * OrderDto -> Order 변환 Mapper. 연관관계 객체들을 생성하면서 매핑하기 때문에, @Mapper를 사용하지 않았습니다.
 */
public class OrderMapper {
  public Order orderDtoToOrder(OrderDto.Post post, Long userId) {
    Order order = Order.builder()
        .store(new Store(post.getStoreId()))
        .request(post.getRequest())
        .user(new User(userId))
        .build();

    for (OrderDto.OrderMenuDto orderMenuDto : post.getMenus()) {
      OrderMenu orderMenu = orderMenuDtoToOrderMenu(orderMenuDto, order);
      order.addOrderMenu(orderMenu);
    }
    return order;
  }

  private OrderMenu orderMenuDtoToOrderMenu(OrderDto.OrderMenuDto orderMenuDto, Order order) {
    OrderMenu orderMenu = OrderMenu.builder()
        .menu(new Menu(orderMenuDto.getMenuId()))
        .order(order)
        .amount(orderMenuDto.getAmount())
        .build();
    for (OrderDto.OrderGroupDto orderGroupDto : orderMenuDto.getGroups()) {
      OrderGroup orderGroup = orderGroupDtoToOrderGroup(orderGroupDto, orderMenu);
      orderMenu.addOrderGroup(orderGroup);
    }
    return orderMenu;
  }

  private OrderGroup orderGroupDtoToOrderGroup(OrderDto.OrderGroupDto orderGroupDto, OrderMenu orderMenu) {
    OrderGroup orderGroup = OrderGroup.builder()
        .orderMenu(orderMenu)
        .group(new Group(orderGroupDto.getGroupId()))
        .build();
    for (Long optionId : orderGroupDto.getOptionIds()) {
      orderGroup.addOrderOption(optionIdToOption(optionId, orderGroup));
    }
    return orderGroup;
  }

  private OrderOption optionIdToOption(Long optionId, OrderGroup orderGroup) {
    return OrderOption.builder()
        .option(new Option(optionId))
        .orderGroup(orderGroup)
        .build();
  }

}
