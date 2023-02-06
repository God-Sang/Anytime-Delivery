package com.godsang.anytimedelivery.store;


import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.store.controller.StoreController;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.mapper.StoreMapper;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
public class StoreControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private StoreController storeController;
  @MockBean
  private StoreService storeService;
  @MockBean
  private StoreMapper storeMapper;
  @Autowired
  private Gson gson;

  private static Stream<Arguments> provideInvalidStoreGetInputs() {
    return Stream.of(
        Arguments.of(0L, null, null),
        Arguments.of(1L, -1, 10),
        Arguments.of(1L, 2, null)
    );
  }

  @DisplayName("유효한 입력")
  @Test
  @WithMockUser(username = "tester", roles = {"CUSTOMER"})
  void findByCategoryTest() throws Exception {
    //given
    List<Store> stores = new ArrayList<>();
    List<StoreDto.GetResponse> dtos = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      Store store = StubData.MockStore.getMockEntity((long) i, "store" + i);
      stores.add(store);
      StoreDto.GetResponse responseDto = new StoreDto.GetResponse();
      responseDto.setName(store.getName());
      dtos.add(responseDto);
    }
    Page<Store> result = new PageImpl<>(stores, PageRequest.of(0, 10), 20);
    given(storeService.findByCategoryId(any(), any()))
        .willReturn(result);
    given(storeMapper.storeListToGetResponseDto(stores))
        .willReturn(dtos);
    StoreDto.GetRequest getRequest = new StoreDto.GetRequest(1L, 0, 10);
    String content = gson.toJson(getRequest);

    //when
    Long categoryId = 1L;

    //when
    ResultActions resultActions = mockMvc.perform(
        get("/customer/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON));
    MvcResult mvcResult = resultActions
        .andExpect(jsonPath("$.data[0].name").exists())
        .andExpect(jsonPath("$.data[9].name").exists())
        .andExpect(jsonPath("$.pageInfo.size").value(10))
        .andReturn();
  }

  @DisplayName("유효하지 않은 입력")
  @ParameterizedTest
  @MethodSource("provideInvalidStoreGetInputs")
  @WithMockUser(username = "tester", roles = {"CUSTOMER"})
  void findByCategoryFailTest(Long categoryId, Integer page, Integer size) throws Exception {
    //given
    StoreDto.GetRequest getRequest = new StoreDto.GetRequest(categoryId, page, size);
    String content = gson.toJson(getRequest);

    //when
    ResultActions resultActions = mockMvc.perform(
        get("/customer/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON));
    MvcResult mvcResult = resultActions
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  @DisplayName("캐시 정장 작동 확인")
  void findByCategoryCacheTest() throws Exception {
    //given
    StoreDto.GetRequest getRequest = new StoreDto.GetRequest(1L, 0, 10);
    String content = gson.toJson(getRequest);

    //when
    for (int i = 0; i < 10; i++) {
      mockMvc.perform(
          get("/customer/stores")
              .contentType(MediaType.APPLICATION_JSON)
              .content(content)
              .accept(MediaType.APPLICATION_JSON));
    }

    //then
    verify(storeService, atMostOnce()).findByCategoryId(any(), any());
  }
}
