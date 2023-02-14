package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.store.dto.StoreDto;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class StoreIntegrationTest {
  private final List<Long> categoryIds = List.of(1L, 2L);
  private final List<String> deliveryAreas = List.of("성남시 분당구 정자동", "성남시 분당구 분당동");
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private DeliveryAreaService deliveryAreaService;
  @Autowired
  private DeliveryAreaRepository deliveryAreaRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private Gson gson;
  private String savedRegistrationNumber;
  private String savedName;
  private String savedTel;
  private String savedAddress;
  private Cookie session;

  @BeforeAll
  void saveEntity() throws Exception {
    savedRegistrationNumber = "123-12-12345";
    savedName = "애니타임 치킨";
    savedTel = "02-1234-5678";
    savedAddress = "서울특별시 강남구 강남대로 123길 12";
    Store store = StubData.MockStore.getMockEntity(1L, savedRegistrationNumber, savedName, savedTel, savedAddress);
    storeRepository.save(store);

    User user = StubData.MockUser.getMockEntity(Role.ROLE_OWNER);
    String password = user.getPassword();
    userService.createUser(user, "owner");

    session = mockMvc.perform(
            post("/users/login")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("email", user.getEmail())
                .param("password", password)
        )
        .andReturn().getResponse().getCookie("SESSION");

  }

  @Test
  @DisplayName("가게등록 성공")
  void createStoreTest() throws Exception {
    // given
    String registrationNumber = "321-21-54321";
    String name = "오빠닥";
    String tel = "031-123-1234";
    String address = "경기도 성남시 분당구 정자동 123";
    String content = gson.toJson(getPostDto(registrationNumber, name, tel, address, categoryIds, deliveryAreas));

    // when
    mockMvc.perform(
            post("/owner/stores")
                .cookie(session)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.storeId").value(2))
        .andExpect(jsonPath("$.data.name").value(name));

    Store store = storeRepository.findById(2L).get();
    assertThat(store.getRegistrationNumber()).isEqualTo(registrationNumber);
    assertThat(store.getCategoryStores().get(0).getCategory().getCategoryId()).isEqualTo(1L);
    assertThat(store.getUser().getRole()).isEqualTo(Role.ROLE_OWNER);

    List<DeliveryArea> deliveryArea = deliveryAreaRepository.findAll();
    assertThat(deliveryArea.size()).isEqualTo(2);
    assertThat(deliveryArea.get(0).getJuso()).isIn(deliveryAreas);

    Page<Store> storePage = storeRepository.findStoresByCategoryAndDeliveryArea(1L, 1L, PageRequest.of(0, 10));
    assertThat(storePage.getContent().get(0).getStoreId()).isEqualTo(2L);
  }

  @Test
  @DisplayName("가게등록 실패: 중복된 사업자 등록번호")
  void duplicatedRegistrationNumberTest() throws Exception {
    //given
    String name = "오빠닥";
    String tel = "031-123-1234";
    String address = "경기도 성남시 분당구 정자동 123";
    String content = gson.toJson(getPostDto(savedRegistrationNumber, name, tel, address, categoryIds, deliveryAreas));

    // when
    mockMvc.perform(
            post("/owner/stores")
                .cookie(session)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.REGISTRATION_NUMBER_ALREADY_EXISTS.getDescription()));
  }

  @Test
  @DisplayName("가게등록 실패: 중복된 가게명")
  void duplicatedNameTest() throws Exception {
    //given
    String registrationNumber = "321-21-54321";
    String tel = "031-123-1234";
    String address = "경기도 성남시 분당구 정자동 123";
    String content = gson.toJson(getPostDto(registrationNumber, savedName, tel, address, categoryIds, deliveryAreas));

    // when
    mockMvc.perform(
            post("/owner/stores")
                .cookie(session)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.NAME_ALREADY_EXISTS.getDescription()));
  }

  @Test
  @DisplayName("가게등록 실패: 중복된 전화번호")
  void duplicatedTelTest() throws Exception {
    //given
    String registrationNumber = "321-21-54321";
    String name = "오빠닥";
    String address = "경기도 성남시 분당구 정자동 123";
    String content = gson.toJson(getPostDto(registrationNumber, name, savedTel, address, categoryIds, deliveryAreas));

    // when
    mockMvc.perform(
            post("/owner/stores")
                .cookie(session)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.TEL_ALREADY_EXISTS.getDescription()));
  }

  @Test
  @DisplayName("가게등록 실패: 중복된 주소")
  void duplicatedAddressTest() throws Exception {
    //given
    String registrationNumber = "321-21-54321";
    String name = "오빠닥";
    String tel = "031-123-1234";
    String content = gson.toJson(getPostDto(registrationNumber, name, tel, savedAddress, categoryIds, deliveryAreas));

    // when
    mockMvc.perform(
            post("/owner/stores")
                .cookie(session)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value(ExceptionCode.ADDRESS_ALREADY_EXISTS.getDescription()));
  }

  @Test
  @DisplayName("배달 가능 지역이 이미 등록되어 있을 경우 생성하지 않는다.")
  void deliveryAreaTest() throws Exception {
    // given
    Store store = storeRepository.findById(1L).get();
    for (String juso : deliveryAreas) {
      DeliveryArea deliveryArea = deliveryAreaService.findExistedDeliveryArea(juso);
      DeliveryAreaStore deliveryAreaStore = new DeliveryAreaStore(store, deliveryArea);
      store.addDeliveryAreaStore(deliveryAreaStore);
    }
    storeRepository.save(store);

    String registrationNumber = "321-21-54321";
    String name = "오빠닥";
    String tel = "031-123-1234";
    String address = "경기도 성남시 분당구 정자동 123";
    String content = gson.toJson(getPostDto(registrationNumber, name, tel, address, categoryIds, deliveryAreas));

    // when
    mockMvc.perform(
        post("/owner/stores")
            .cookie(session)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
    );

    // then
    List<DeliveryArea> deliveryArea = deliveryAreaRepository.findAll();
    assertThat(deliveryArea.size()).isEqualTo(2);
  }

  private StoreDto.Post getPostDto(String registrationNumber, String name, String tel, String address, List<Long> categoryIds, List<String> deliveryAreas) {
    return StubData.MockStorePost.builder()
        .registrationNumber(registrationNumber)
        .name(name)
        .tel(tel)
        .address(address)
        .deliveryFee(1000)
        .deliveryTime(30)
        .openTime("00:00")
        .closeTime("23:59")
        .categoryIds(categoryIds)
        .deliveryAreas(deliveryAreas)
        .build();
  }
}
