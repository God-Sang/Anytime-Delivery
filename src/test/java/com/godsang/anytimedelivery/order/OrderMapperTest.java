package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class OrderMapperTest {
  private OrderMapper orderMapper = new OrderMapper();
  @Test
  @DisplayName("OrderDto -> Order test")
  void mapperTest() {
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
}
