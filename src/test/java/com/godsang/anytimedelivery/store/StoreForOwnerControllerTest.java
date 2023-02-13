package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.store.controller.StoreForOwnerController;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.mapper.StoreMapper;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = StoreForOwnerController.class,
    includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(roles = "OWNER")
public class StoreForOwnerControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private StoreService storeService;
  @MockBean
  private StoreMapper storeMapper;
  @Autowired
  private Gson gson;
  private String registrationNumber;
  private String name;
  private String tel;
  private int deliveryFee;
  private int deliveryTime;
  private String openTime;
  private List<Long> categoryIds;
  private List<String> deliveryAreas;

  static Stream<Arguments> getInvalidCategoryIds() {
    return Stream.of(
        Arguments.of(List.of(1, 1)),
        Arguments.of(List.of(0, -1)),
        Arguments.of(List.of("치킨")),
        Arguments.of(List.of()),
        Arguments.of(List.of(1, 2, 3, 4))
    );
  }

  static Stream<Arguments> getInvalidDeliveryAreas() {
    return Stream.of(
        Arguments.of(List.of("강남구 신사동")),
        Arguments.of(List.of("서울특별시 강남구 역삼동", "서울특별시 강남구 역삼동")),
        Arguments.of(List.of("경기도 성남시")),
        Arguments.of(List.of("seoul")),
        Arguments.of(List.of("1", "2")),
        Arguments.of(List.of()),
        Arguments.of(List.of("도 군 읍"))
    );
  }

  @BeforeEach
  void assignValue() {
    registrationNumber = "123-12-12345";
    name = "애니타임 치킨";
    tel = "02-123-4567";
    deliveryFee = 3000;
    deliveryTime = 60;
    openTime = "00:00";
    categoryIds = List.of(1L);
    deliveryAreas = List.of("서울특별시 강남구 역삼동", "서울특별시 강남구 삼성동");
  }

  @Test
  @DisplayName("가게 등록 성공")
  void createStoreSuccessTest() throws Exception {
    // given
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isCreated());
  }

  @ParameterizedTest
  @DisplayName("올바르지 않은 사업자 등록번호: 400 error")
  @ValueSource(strings = {"12-12-12345", "123-1-12345", "123-12-1234", "1234-12-1234", "111_11_11111", "사업자-등록-번호"})
  void invalidRegistrationNumberTest(String invalidRegistrationNumber) throws Exception {
    // given
    registrationNumber = invalidRegistrationNumber;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @DisplayName("올바르지 않은 가게명: 400 error")
  @ValueSource(strings = {"치킨#집", " ", "@골뱅이 소면", "ㄱ피자나라"})
  void invalidNameTest(String invalidName) throws Exception {
    // given
    name = invalidName;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @DisplayName("올바른 가게명: 201 created")
  @ValueSource(strings = {"치킨집 강남점", "BBQ", "the맛나", "Pizza 허걱", "엽떡 1호점"})
  void validNameTest(String validName) throws Exception {
    // given
    name = validName;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isCreated());
  }

  @ParameterizedTest
  @DisplayName("올바르지 않은 전화번호: 400 error")
  @ValueSource(strings = {"02-12345-2345", "0401-111-2334", "02-369-369", "전화번호", "02-1234-사오육칠"})
  void invalidTelTest(String invalidTel) throws Exception {
    // given
    tel = invalidTel;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @DisplayName("올바르지 않은 오픈 시간: 400 error")
  @ValueSource(strings = {"00:60", "24:00", "23:60", "10:97", "03;10", "12.40", "오전 다섯시 오십분", "08:30 pm"})
  void invalidOpenTimeTest(String invalidOpenTime) throws Exception {
    // given
    openTime = invalidOpenTime;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @DisplayName("올바른 오픈 시간: 400 error")
  @ValueSource(strings = {"00:00", "23:59", "19:59", "09:09", "21:21", "11:11"})
  void validOpenTimeTest(String validOpenTime) throws Exception {
    // given
    openTime = validOpenTime;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isCreated());
  }

  @ParameterizedTest
  @DisplayName("올바르지 않은 카테고리 아이디: 400 error")
  @MethodSource("getInvalidCategoryIds")
  void invalidCategoryIdsTest(List<Long> invalidCategoryIds) throws Exception {
    // given
    categoryIds = invalidCategoryIds;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());

  }

  @ParameterizedTest
  @DisplayName("올바르지 않은 배달 가능 지역: 400 error")
  @MethodSource("getInvalidDeliveryAreas")
  void invalidDeliveryAreasTest(List<String> invalidDeliveryAreas) throws Exception {
    // given
    deliveryAreas = invalidDeliveryAreas;
    String content = gson.toJson(getPostDto());

    // when
    mockMvc.perform(
            post("/owner/stores")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isBadRequest());
  }

  private StoreDto.Post getPostDto() {
    return StubData.MockStorePost.builder()
        .registrationNumber(registrationNumber)
        .name(name)
        .address("서울특별시 강남구 강남대로 123길 101호")
        .tel(tel)
        .deliveryFee(deliveryFee)
        .deliveryTime(deliveryTime)
        .openTime(openTime)
        .closeTime("23:59")
        .categoryIds(categoryIds)
        .deliveryAreas(deliveryAreas)
        .build();
  }
}
