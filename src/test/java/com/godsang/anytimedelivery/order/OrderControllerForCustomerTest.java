package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.annotation.WithMockCustomUser;
import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.order.controller.OrderForCustomerController;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.service.OrderService;
import com.godsang.anytimedelivery.user.entity.Role;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OrderForCustomerController.class,
    includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@MockBean(JpaMetamodelMappingContext.class)
@WithMockCustomUser(role = Role.ROLE_CUSTOMER)
public class OrderControllerForCustomerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private OrderService orderService;
  @MockBean
  private OrderMapper orderMapper;
  @Autowired
  private Gson gson;

  @DisplayName("주문 요청")
  @Test
  void requestOrderTest() throws Exception {
    OrderDto.Post post = MockDto.OrderPost.get();
    String content = gson.toJson(post);

    // when
    mockMvc.perform(
            post("/orders")
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
        // then
        .andExpect(status().isCreated());
  }

  @DisplayName("주문 취소")
  @Test
  void cancelOrderTest() throws Exception {
    Long orderId = 1L;

    // when
    mockMvc.perform(
            patch("/orders/{order-id}", orderId)
                .accept(MediaType.APPLICATION_JSON)
        )
        // then
        .andExpect(status().isOk());
  }

  @DisplayName("주문 상세 조회")
  @Test
  void retrieveOrderTest() throws Exception {
    //given
    Long orderId = 1L;
    OrderDto.Response response = MockDto.OrderResponse.get(OrderStatus.WAIT);
    given(orderMapper.orderToResponse(any())).willReturn(response);

    // when
    mockMvc.perform(
            get("/orders/{order-id}", orderId)
                .accept(MediaType.APPLICATION_JSON)
        )
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.menus[0].groups[0].options[0].name")
            .value(response.getMenus().get(0).getGroups().get(0).getOptions().get(0).getName()));
  }

  @DisplayName("주문 리스트 조회")
  @Test
  void retrieveOrderListTest() throws Exception {
    //given
    int page = 1;
    int size = 5;
    List<OrderDto.ResponseForList> responses = MockDto.OrderResponse.getList();
    given(orderMapper.ordersToResponses(any())).willReturn(responses);

    // when
    mockMvc.perform(
            get("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("page", String.valueOf(page))
                .queryParam("size", String.valueOf(size))
        )
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].address").value(responses.get(0).getAddress()));
  }
}