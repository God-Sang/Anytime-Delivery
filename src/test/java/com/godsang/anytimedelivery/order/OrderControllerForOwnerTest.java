package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.annotation.WithMockCustomUser;
import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.order.controller.OrderForOwnerController;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.service.OrderService;
import com.godsang.anytimedelivery.user.entity.Role;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OrderForOwnerController.class,
    includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@MockBean(JpaMetamodelMappingContext.class)
@WithMockCustomUser(role = Role.ROLE_OWNER)
public class OrderControllerForOwnerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private OrderService orderService;
  @MockBean
  private OrderMapper orderMapper;
  @Autowired
  private Gson gson;

  @Test
  @DisplayName("주문 리스트 조회")
  void retrieveOrdersTest() throws Exception {
    // given
    Long storeId = 1L;
    MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
    queries.add("order-state", "WAIT");
    queries.add("page", String.valueOf(1));
    queries.add("size", String.valueOf(5));
    List<OrderDto.ResponseForList> responses = MockDto.OrderResponse.getList();
    given(orderMapper.ordersToResponses(any())).willReturn(responses);

    // when
    mockMvc.perform(
            get("/owner/stores/{store-id}/orders", storeId)
                .queryParams(queries)
                .accept(MediaType.APPLICATION_JSON)

        )
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].address").value(responses.get(0).getAddress()));
  }

  @Test
  @DisplayName("주문 리스트 조회 예외 : Enum에 없는 값을 입력한 경우")
  void retrieveOrdersExceptionTest() throws Exception {
    // given
    Long storeId = 1L;
    MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
    queries.add("order-state", "WAI");
    queries.add("page", String.valueOf(1));
    queries.add("size", String.valueOf(5));
    List<OrderDto.ResponseForList> responses = MockDto.OrderResponse.getList();
    given(orderMapper.ordersToResponses(any())).willReturn(responses);

    // when
    mockMvc.perform(
            get("/owner/stores/{store-id}/orders", storeId)
                .queryParams(queries)
                .accept(MediaType.APPLICATION_JSON)

        )
        // then
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(startsWith("A value")));
  }

  @Test
  @DisplayName("주문 상태 변경 조회")
  void changeOrderStateTest() throws Exception {
    // given
    Long storeId = 1L;
    Long orderId = 1L;
    OrderDto.Patch patch = MockDto.OrderPatch.get(OrderStatus.ACCEPTED);
    String content = gson.toJson(patch);
    // when
    mockMvc.perform(
            patch("/owner/stores/{store-id}/orders/{order_id}", storeId, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {"WAIT", "CANCELED"})
  @DisplayName("주문 상태 변경 조회 예외 : WAIT / CANCELED 로 변경하는 경우")
  void changeOrderStateExceptionTest(String orderStatus) throws Exception {
    // given
    Long storeId = 1L;
    Long orderId = 1L;
    OrderDto.Patch patch = MockDto.OrderPatch.get(OrderStatus.valueOf(orderStatus));
    String content = gson.toJson(patch);
    // when
    mockMvc.perform(
            patch("/owner/stores/{store-id}/orders/{order_id}", storeId, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("주문 취소")
  void cancelOrderTest() throws Exception {
    // given
    Long storeId = 1L;
    Long orderId = 1L;
    OrderDto.PatchCancel patchCancel = MockDto.OrderPatchCancel.get("재료 소진");
    String content = gson.toJson(patchCancel);
    // when
    mockMvc.perform(
            patch("/owner/stores/{store-id}/orders/{order_id}/cancel", storeId, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {" ", ""})
  @DisplayName("주문 취소 예외 : 사유가 blank인 경우")
  void cancelOrderExceptionTest(String reason) throws Exception {
    // given
    Long storeId = 1L;
    Long orderId = 1L;
    OrderDto.PatchCancel patchCancel = MockDto.OrderPatchCancel.get(reason);
    String content = gson.toJson(patchCancel);
    // when
    mockMvc.perform(
            patch("/owner/stores/{store-id}/orders/{order_id}/cancel", storeId, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }
}
