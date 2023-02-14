package com.godsang.anytimedelivery.store;


import com.godsang.anytimedelivery.auth.utils.LoggedInUserInfoUtils;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.store.controller.StoreForCustomerController;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.mapper.StoreMapper;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * custom SpringSecurity configuration을 적용하기 위해 @EnableWebSecurity 어노테이션을 스캔하도록 필터 포함
 *
 * @WebMvcTest 어노테이션은 SpringSecurity에서 자동으로 구성하는 기본 configuration 파일을 스캔하기 때문
 */
@WebMvcTest(value = StoreForCustomerController.class,
    includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@MockBean(JpaMetamodelMappingContext.class)
public class StoreForCustomerControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private StoreService storeService;
  @MockBean
  private StoreMapper storeMapper;
  @MockBean
  private LoggedInUserInfoUtils loggedInUserInfoUtils;
  @Autowired
  private Gson gson;

  private static Stream<Arguments> provideInvalidStoreGetInputs() {
    return Stream.of(
        Arguments.of(0L, null, null),
        Arguments.of(1L, -1, 10),
        Arguments.of(1L, 2, null)
    );
  }

  @Test
  @DisplayName("유효한 입력")
  @WithMockUser(username = "tester", roles = {"CUSTOMER"})
  void findByCategoryTest() throws Exception {
    //given
    List<Store> stores = new ArrayList<>();
    List<StoreDto.Response> dtos = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      Store store = StubData.MockStore.getMockEntity((long) i, "111-11", "store" + i, "02-123", "강남구");
      stores.add(store);
      StoreDto.Response responseDto = new StoreDto.Response();
      responseDto.setName(store.getName());
      dtos.add(responseDto);
    }
    Page<Store> result = new PageImpl<>(stores, PageRequest.of(1, 10), 20);
    given(loggedInUserInfoUtils.extractUserId()).willReturn(1L);
    given(storeService.findStoresByCategoryId(anyLong(), anyLong(), any())).willReturn(result);
    given(storeMapper.storeListToResponseDto(stores)).willReturn(dtos);
    MultiValueMap<String, String> queries = StubData.Query.getPageQuery(1, 10);

    //when
    mockMvc.perform(
            get("/categories/{category-id}/stores", 1L)
                .queryParams(queries)
                .accept(MediaType.APPLICATION_JSON)
        )
        //then
        .andExpect(jsonPath("$.data[0].name").exists())
        .andExpect(jsonPath("$.data[9].name").exists())
        .andExpect(jsonPath("$.pageInfo.size").value(10));
  }

  @DisplayName("유효하지 않은 입력")
  @ParameterizedTest
  @MethodSource("provideInvalidStoreGetInputs")
  @WithMockUser(username = "tester", roles = {"CUSTOMER"})
  void findByCategoryFailTest(Long categoryId, Integer page, Integer size) throws Exception {
    //given
    MultiValueMap<String, String> queries = StubData.Query.getPageQuery(page, size);

    //when
    mockMvc.perform(
            get("/categories/{category-id}/stores", categoryId)
                .queryParams(queries)
                .accept(MediaType.APPLICATION_JSON)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "OWNER")
  @DisplayName("OWNER 권한으로 store 조회 시 failure")
  void requestWithOwnerTest() throws Exception {
    // given
    MultiValueMap<String, String> queries = StubData.Query.getPageQuery(1, 10);

    // when
    mockMvc.perform(
            get("/categories/{category-id}/stores", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .queryParams(queries)
        )
        // then
        .andExpect(status().isForbidden());
  }
}
