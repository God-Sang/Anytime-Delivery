package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.annotation.WithMockCustomUser;
import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.order.controller.OrderForOwnerController;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.service.OrderService;
import com.godsang.anytimedelivery.store.controller.StoreForCustomerController;
import com.godsang.anytimedelivery.user.entity.Role;
import com.google.gson.Gson;
import jdk.jfr.ContentType;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
