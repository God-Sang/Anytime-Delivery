package com.godsang.anytimedelivery.order.mapper;

import com.godsang.anytimedelivery.address.entity.Address;
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
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OrderDto -> Order 변환 Mapper. 연관관계 객체들을 생성하면서 매핑하기 때문에, @Mapper를 사용하지 않았습니다.
 */
@Component
@Mapper(componentModel = "spring")
public interface OrderMapper {
  List<OrderDto.ResponseForList> ordersToResponses(List<Order> orders);

  default OrderDto.ResponseForList orderToResponseForList(Order order) {
    Address address = order.getUser().getAddress();
    return OrderDto.ResponseForList.builder()
        .orderId(order.getOrderId())
        .storeName(order.getStore().getName())
        .address(address.getAddress())
        .detailAddress(address.getDetailAddress())
        .request(order.getRequest())
        .orderTime(order.getCreatedAt())
        .foodTotalPrice(order.getFoodTotalPrice())
        .deliveryTime(order.getStore().getDeliveryTime())
        .deliveryFee(order.getStore().getDeliveryFee())
        .build();
  }

  default OrderDto.Response orderToResponse(Order order) {
    return OrderDto.Response.builder()
        .orderId(order.getOrderId())
        .storeName(order.getStore().getName())
        .foodTotalPrice(order.getFoodTotalPrice())
        .orderStatus(order.getStatus().name())
        .customer(userToCustomer(order.getUser()))
        .orderTime(order.getCreatedAt())
        .menus(orderMenusToMenuResponses(order.getOrderMenus()))
        .deliveryFee(order.getStore().getDeliveryFee())
        .deliveryTime(order.getStore().getDeliveryTime())
        .build();
  }

  List<OrderDto.MenuResponse> orderMenusToMenuResponses(List<OrderMenu> orderMenus);

  default OrderDto.MenuResponse orderMenuToMenuResponse(OrderMenu orderMenu) {
    return OrderDto.MenuResponse.builder()
        .name(orderMenu.getMenu().getName())
        .price(orderMenu.getMenu().getPrice())
        .amount(orderMenu.getAmount())
        .groups(orderGroupsToGroupResponses(orderMenu.getOrderGroups()))
        .build();
  }

  List<OrderDto.GroupResponse> orderGroupsToGroupResponses(List<OrderGroup> orderGroups);
  default OrderDto.GroupResponse orderGroupToGroupResponse(OrderGroup orderGroup) {
    return OrderDto.GroupResponse.builder()
        .title(orderGroup.getGroup().getTitle())
        .options(orderOptionsToOptionResponses(orderGroup.getOrderOptions()))
        .build();
  }

  List<OrderDto.OptionResponse> orderOptionsToOptionResponses(List<OrderOption> orderOptions);

  default OrderDto.OptionResponse orderOptionToOptionResponse(OrderOption orderOption) {
    return OrderDto.OptionResponse.builder()
        .name(orderOption.getOption().getName())
        .price(orderOption.getOption().getPrice())
        .build();
  }

  default OrderDto.Customer userToCustomer(User user) {
    return OrderDto.Customer.builder()
        .nickName(user.getNickName())
        .phone(user.getPhone())
        .address(user.getAddress().getAddress())
        .detailAddress(user.getAddress().getDetailAddress())
        .build();
  }
  default Order orderDtoToOrder(OrderDto.Post post, Long userId) {
    Order order = Order.builder()
        .request(post.getRequest())
        .foodTotalPrice(post.getFoodTotalPrice())
        .store(new Store(post.getStoreId()))
        .user(new User(userId))
        .build();
    for (OrderDto.OrderMenuDto orderMenuDto : post.getMenus()) {
      OrderMenu orderMenu = orderMenuDtoToOrderMenu(orderMenuDto, order);
      order.addOrderMenu(orderMenu);
    }
    return order;
  }

  default OrderMenu orderMenuDtoToOrderMenu(OrderDto.OrderMenuDto orderMenuDto, Order order) {
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

  default OrderGroup orderGroupDtoToOrderGroup(OrderDto.OrderGroupDto orderGroupDto, OrderMenu orderMenu) {
    OrderGroup orderGroup = OrderGroup.builder()
        .orderMenu(orderMenu)
        .group(new Group(orderGroupDto.getGroupId()))
        .build();
    for (Long optionId : orderGroupDto.getOptionIds()) {
      orderGroup.addOrderOption(optionIdToOption(optionId, orderGroup));
    }
    return orderGroup;
  }

  default OrderOption optionIdToOption(Long optionId, OrderGroup orderGroup) {
    return OrderOption.builder()
        .option(new Option(optionId))
        .orderGroup(orderGroup)
        .build();
  }

}
