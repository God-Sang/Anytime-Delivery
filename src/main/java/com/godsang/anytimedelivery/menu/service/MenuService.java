package com.godsang.anytimedelivery.menu.service;

import com.godsang.anytimedelivery.auth.utils.LoggedInUserInfoUtils;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;
  private final StoreService storeService;
  private final LoggedInUserInfoUtils loggedInUserInfoUtils;

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
  public Menu createMenu(long storeId, Menu menu, Group group) {
    Store store = storeService.findStoreById(storeId);
    verifyStoreOwner(store);
    verifyStoreHasSameMenu(store, menu.getName());

    menu.setStore(store);
    menu.addGroup(group);
    group.setMenu(menu);
    return menuRepository.save(menu);
  }

  /**
   * Context Holder의 User와 가게의 사장님이 일치하는지 확인
   *
   * @throws BusinessLogicException when owner does not match.
   * @Param store 가게
   */
  private void verifyStoreOwner(Store store) {
    long userId = loggedInUserInfoUtils.extractUserId();
    User storeOwner = store.getUser();
    if (userId != storeOwner.getUserId()) {
      throw new BusinessLogicException(ExceptionCode.OWNER_NOT_MATCHED);
    }
  }

  /**
   * 등록하려는 메뉴명이 가게에 이미 존재하는지 확인
   *
   * @throws BusinessLogicException when owner create same menu
   * @Param store 가게
   * @Param name 메뉴명
   */
  private void verifyStoreHasSameMenu(Store store, String name) {
    if (menuRepository.existsByNameAndStore(name, store)) {
      throw new BusinessLogicException(ExceptionCode.MENU_ALREADY_EXISTS);
    }
  }
}
