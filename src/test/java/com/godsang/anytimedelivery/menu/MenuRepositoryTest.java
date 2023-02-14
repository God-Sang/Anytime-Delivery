package com.godsang.anytimedelivery.menu;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuRepositoryTest {
  private final String registrationNumber = "123-12-12345";
  private final String title = "치즈볼 택 1";
  private final String choiceType = "check";
  private final String[] optionName = {"기본 치즈볼", "초코 치즈볼", "크림 치즈볼"};
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private StoreRepository storeRepository;

  @BeforeAll
  void saveEntity() {
    Store store = StubData.MockStore.getMockEntity(1L, registrationNumber, "애니타임 치킨", "02-123-1234", "서울특별시 강남구 강남대로 123");
    storeRepository.save(store);

    List<Option> options = new ArrayList<>();
    for (int i = 0; i < optionName.length; i++) {
      options.add(StubData.MockMenu.getOption(optionName[i], 5000));
    }
    Group group = StubData.MockMenu.getMockGroupEntity(title, choiceType, options);
    Menu menu = StubData.MockMenu.getMockMenuEntity("푸다닥 치킨", 30000);
    menu.addGroup(group);
    menu.setStore(store);
    group.setMenu(menu);

    menuRepository.save(menu);
  }

  @Test
  @DisplayName("객체 그래프 탐색")
  void instanceSearchTest() {
    // when
    Menu menu = menuRepository.findById(1L).get();

    // then
    // 가게의 사업자 등록번호
    assertThat(menu.getStore().getRegistrationNumber()).isEqualTo(registrationNumber);
    // 그룹
    Group group = menu.getGroups().get(0);
    assertThat(group.getTitle()).isEqualTo(title);
    assertThat(group.getChoiceType()).isEqualTo(choiceType);
    // 옵션
    List<Option> options = group.getOptions();
    assertThat(options.get(0).getName()).isIn((Object[]) optionName);
  }
}
