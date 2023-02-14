package com.godsang.anytimedelivery.menu.service;

import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import com.godsang.anytimedelivery.menu.entity.OptionGroup;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;
  private final StoreService storeService;

  /**
   * 메뉴 등록
   *
   * @Param storeId 가게 아아디
   * @Param menu 메뉴
   * @Param group 옵션 그룹
   * @Param options 메뉴 옵션
   * @Return Menu
   */
  @Transactional
  public Menu createMenu(long storeId, Menu menu, Group group, List<Option> options) {
    Store store = storeService.findStoreById(storeId);
    menu.setStore(store);
    menu.addGroup(createGroup(menu, group, options));
    return menuRepository.save(menu);
  }

  /**
   * Group, Option N:M mapping
   */
  private Group createGroup(Menu menu, Group group, List<Option> options) {
    group.setMenu(menu);
    for (Option option : options) {
      OptionGroup optionGroup = new OptionGroup(group, option);
      group.addOptionGroup(optionGroup);
    }
    return group;
  }
}
