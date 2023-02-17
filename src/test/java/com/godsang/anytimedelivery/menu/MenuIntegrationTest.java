package com.godsang.anytimedelivery.menu;

import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.helper.annotation.WithMockCustomUser;
import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.entity.ChoiceType;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@WithMockCustomUser(role = Role.ROLE_OWNER)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenuIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Gson gson;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private StoreService storeService;
  private Long storeId;

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.getMockEntity(Role.ROLE_OWNER);
    userRepository.save(user);

    String savedRegistrationNumber = "123-12-12345";
    String savedName = "애니타임 치킨";
    String savedTel = "02-1234-5678";
    String savedAddress = "서울특별시 강남구 강남대로 123길 12";
    Store store = StubData.MockStore.getMockEntity(1L, savedRegistrationNumber, savedName, savedTel, savedAddress);
    store.setUser(user);
    Store savedStore = storeRepository.save(store);
    storeId = savedStore.getStoreId();
  }

  @Test
  @DisplayName("메뉴등록 성공")
  @Order(100)
  void createStoreTest() throws Exception {
    // given
    MenuDto.Post post = MockDto.MenuPost.getOption("치킨", 20000, ChoiceType.RADIO.name());
    String content = gson.toJson(post);

    // when
    mockMvc.perform(
            post("/owner/stores/{store-id}", storeId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.name").value(post.getName()))
        .andExpect(jsonPath("$.data.groups[0].title").value(post.getGroups().get(0).getTitle()))
        .andExpect(jsonPath("$.data.groups[0].options[0].name").value(post.getGroups().get(0).getOptions().get(0).getName()));

    Menu menu = menuRepository.findById(1L).get();
    assertThat(menu.getStore().getStoreId()).isEqualTo(storeId);
    assertThat(menu.getGroups().get(0).getChoiceType()).isEqualTo(ChoiceType.RADIO);

    String optionName = menu.getGroups().get(0).getOptions().get(0).getName();
    assertThat(optionName).isEqualTo(post.getGroups().get(0).getOptions().get(0).getName());
  }

  @Test
  @DisplayName("가게의 메뉴 조회")
  @WithMockCustomUser(role = Role.ROLE_CUSTOMER)
  @Order(200)
  void findMenusTest() throws Exception {
    // given
    Store store = storeService.findStoreById(storeId);
    Menu menu = StubData.MockMenu.getMockMenu();
    menu.setStore(store);
    menuRepository.save(menu);

    // when
    mockMvc.perform(
            get("/categories/{category-id}/stores/{store-id}/menu", 1L, storeId)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(jsonPath("$.data[0].menuId").value(2))
        .andExpect(jsonPath("$.data[0].groups[1].choiceType").value("RADIO"))
        .andExpect(jsonPath("$.data[0].groups[1].options[3].price").value(1000));

  }
}
