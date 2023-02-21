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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderDto -> Order 변환 Mapper. 연관관계 객체들을 생성하면서 매핑하기 때문에, @Mapper를 사용하지 않았습니다.
 */
@Component
public class OrderMapper {
  public List<OrderDto.ResponseForList> ordersToResponses(List<Order> orders) {
    List<OrderDto.ResponseForList> responses = new ArrayList<>();
    for (Order order : orders) {
      responses.add(orderToResponseForList(order));
    }
    return responses;
  }

  private OrderDto.ResponseForList orderToResponseForList(Order order) {
    Address address = order.getUser().getAddress();
    return OrderDto.ResponseForList.builder()
        .orderId(order.getOrderId())
        .address(address.getAddress())
        .detailAddress(address.getDetailAddress())
        .request(order.getRequest())
        .deliveryTime(order.getDeliveryTime())
        .orderTime(order.getCreatedAt())
        .foodTotalPrice(order.getFoodTotalPrice())
        .build();
  }
  public OrderDto.Response orderToResponse(Order order) {
    return OrderDto.Response.builder()
        .orderId(order.getOrderId())
        .deliveryFee(order.getDeliveryFee())
        .deliveryTime(order.getDeliveryTime())
        .foodTotalPrice(order.getFoodTotalPrice())
        .orderStatus(order.getStatus().name())
        .customer(userToCustomer(order.getUser()))
        .orderTime(order.getCreatedAt())
        .menus(orderMenusToMenuResponses(order.getOrderMenus()))
        .build();
  }

  private List<OrderDto.MenuResponse> orderMenusToMenuResponses(List<OrderMenu> orderMenus) {
    List<OrderDto.MenuResponse> menuResponses = new ArrayList<>();
    for (OrderMenu orderMenu : orderMenus) {
      menuResponses.add(orderMenuToMenuResponse(orderMenu));
    }
    return menuResponses;
  }

  private OrderDto.MenuResponse orderMenuToMenuResponse(OrderMenu orderMenu) {
    return OrderDto.MenuResponse.builder()
        .name(orderMenu.getMenu().getName())
        .price(orderMenu.getMenu().getPrice())
        .amount(orderMenu.getAmount())
        .groups(orderGroupsToGroupResponses(orderMenu.getOrderGroups()))
        .build();
  }

  private List<OrderDto.GroupResponse> orderGroupsToGroupResponses(List<OrderGroup> orderGroups) {
    List<OrderDto.GroupResponse> groupResponses = new ArrayList<>();
    for (OrderGroup orderGroup : orderGroups) {
      groupResponses.add(orderGroupToGroupResponse(orderGroup));
    }
    return groupResponses;
  }
  private OrderDto.GroupResponse orderGroupToGroupResponse(OrderGroup orderGroup) {
    return OrderDto.GroupResponse.builder()
        .title(orderGroup.getGroup().getTitle())
        .optionResponses(orderOptionsToOptionResponses(orderGroup.getOrderOptions()))
        .build();
  }

  private List<OrderDto.OptionResponse> orderOptionsToOptionResponses(List<OrderOption> orderOptions) {
    List<OrderDto.OptionResponse> optionResponses = new ArrayList<>();
    for (OrderOption orderOption : orderOptions) {
      optionResponses.add(orderOptionToOptionResponse(orderOption));
    }
    return optionResponses;
  }

  private OrderDto.OptionResponse orderOptionToOptionResponse(OrderOption orderOption) {
    return OrderDto.OptionResponse.builder()
        .name(orderOption.getOption().getName())
        .price(orderOption.getOption().getPrice())
        .build();
  }

  private OrderDto.Customer userToCustomer(User user) {
    return OrderDto.Customer.builder()
        .nickName(user.getNickName())
        .phone(user.getPhone())
        .address(user.getAddress().getAddress())
        .detailAddress(user.getAddress().getDetailAddress())
        .build();
  }
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
