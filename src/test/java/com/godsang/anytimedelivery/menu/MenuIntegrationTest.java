package com.godsang.anytimedelivery.menu;

import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
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
public class MenuIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Gson gson;
  @Autowired
  private UserService userService;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private MenuRepository menuRepository;
  private String savedRegistrationNumber;
  private String savedName;
  private String savedTel;
  private String savedAddress;
  private Cookie session;
  private Long storeId;

  @BeforeAll
  void saveEntity() throws Exception {
    User user = StubData.MockUser.getMockEntity(Role.ROLE_OWNER);
    String password = user.getPassword();
    User savedUser = userService.createUser(user, "owner");

    savedRegistrationNumber = "123-12-12345";
    savedName = "애니타임 치킨";
    savedTel = "02-1234-5678";
    savedAddress = "서울특별시 강남구 강남대로 123길 12";
    Store store = StubData.MockStore.getMockEntity(1L, savedRegistrationNumber, savedName, savedTel, savedAddress);
    store.setUser(savedUser);
    Store savedStore =  storeRepository.save(store);
    storeId = savedStore.getStoreId();


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
    MenuDto.Post post = StubData.MockMenuPost.getMenuDto();
    String content = gson.toJson(post);

    // when
    mockMvc.perform(
            post("/owner/stores/" + storeId)
                .cookie(session)
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

    String optionName = menu.getGroups().get(0).getOptions().get(0).getName();
    assertThat(optionName).isEqualTo(post.getGroups().get(0).getOptions().get(0).getName());
  }
}
