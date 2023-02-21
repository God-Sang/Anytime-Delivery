package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class OrderMapperTest {
  @Autowired
  private OrderMapper orderMapper;
  @Test
  @DisplayName("OrderDto -> Order test")
  void orderDtoToOrderTest() {
    //given
    OrderDto.Post post = MockDto.OrderPost.get();

    //when
    Order order = orderMapper.orderDtoToOrder(post, 1L);

    //then
    assertThat(order.getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().get(0).getOrderGroup())
        .isNotNull();
    assertThat(order.getOrderMenus().get(0).getMenu())
        .isNotNull();
  }

  @Test
  @DisplayName("Orders -> OrderResponseDtos Test")
  void ordersToResponsesTest() {
    //given
    List<Order> orders = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      orders.add(StubData.MockOrder.getMockOrder(OrderStatus.ACCEPTED));
    }

    //when
    List<OrderDto.ResponseForList> responses = orderMapper.ordersToResponses(orders);

    //then
    assertThat(responses.size()).isEqualTo(3);
    assertThat(responses.get(0).getAddress()).isNotNull();
  }

  @Test
  @DisplayName("Order -> OrderResponseDtd Test")
  void orderToResponseTest() {
    //given
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.ACCEPTED);

    //when
    OrderDto.Response response = orderMapper.orderToResponse(order);

    //then
    assertThat(response.getCustomer().getDetailAddress())
        .isEqualTo(order.getUser().getAddress().getDetailAddress());
    assertThat(response.getMenus().get(0).getName())
        .isEqualTo(order.getOrderMenus().get(0).getMenu().getName());
    assertThat(response.getMenus().get(0).getGroups().get(0).getOptionResponses().get(0).getPrice())
        .isEqualTo(order.getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().get(0).getOption().getPrice());
  }
}
