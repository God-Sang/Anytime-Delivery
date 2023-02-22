package com.godsang.anytimedelivery.menu;

import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.menu.service.MenuService;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenuServiceCachingTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private StoreRepository storeRepository;
  @MockBean
  private MenuRepository menuRepository;
  @MockBean
  private StoreService storeService;
  @Autowired
  private MenuService menuService;
  private long userId;
  private long storeId;
  private Store savedStore;

  @BeforeAll
  void saveEntity() {
    User user = StubData.MockUser.getMockEntity(Role.ROLE_OWNER);
    User savedUser = userRepository.save(user);
    userId = savedUser.getUserId();

    Store store = StubData.MockStore.getMockEntity();
    store.setUser(user);
    savedStore = storeRepository.save(store);

    Menu menu = StubData.MockMenu.getMockMenu(savedStore);
    menuRepository.save(menu);

    storeId = savedStore.getStoreId();
  }

  @Test
  @DisplayName("가게의 메뉴를 처음으로 조회시 메서드 작동")
  @Order(100)
  void findMenuTest() {
    // when
    menuService.findStoreMenus(storeId);

    // then
    verify(menuRepository, times(1)).findMenusByStoreId(anyLong());
  }

  @RepeatedTest(10)
  @DisplayName("캐시가 저장된 이후 가게의 메뉴 조회 시 캐시 작동 확인, 10번 반복")
  @Order(101)
  void cacheableTest() {
    // when
    menuService.findStoreMenus(storeId);

    // then
    verify(menuRepository, times(0)).findMenusByStoreId(anyLong());
  }

  @Test
  @DisplayName("가게의 메뉴 생성 후 캐시가 삭제되었는지 확인")
  @Order(200)
  void cacheEvictTest() {
    // given
    when(storeService.findStoreById(anyLong())).thenReturn(savedStore);
    Menu newMenu = StubData.MockMenu.getMockMenu("황금 올리브", 20000);
    menuService.createMenu(storeId, userId, newMenu);

    // when
    menuService.findStoreMenus(storeId);

    // then
    verify(menuRepository, times(1)).findMenusByStoreId(anyLong());
  }
}