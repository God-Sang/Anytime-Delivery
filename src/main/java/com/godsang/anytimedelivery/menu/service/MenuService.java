package com.godsang.anytimedelivery.menu.service;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.GroupRepository;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.service.StoreService;
import com.godsang.anytimedelivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;
  private final GroupRepository groupRepository;
  private final StoreService storeService;

  /**
   * 메뉴 등록
   *
   * @Param storeId 가게 아아디
   * @Param userId 사장님 아이디
   * @Param menu 메뉴
   * @Return Menu
   */
  @Transactional
  @CacheEvict(cacheNames = "menu", key = "#storeId")
  public Menu createMenu(long storeId, long userId, Menu menu) {
    Store store = storeService.findStoreById(storeId);
    storeService.verifyStoreOwner(store, userId);
    verifyStoreHasSameMenu(store, menu.getName());

    menu.setStore(store);
    return menuRepository.save(menu);
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

  /**
   * 가게의 메뉴 리스트 조회
   *
   * @Param storeId 가게 아이디
   * @Return menus
   */
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "menu", key = "#storeId")
  public List<Menu> findStoreMenus(long storeId) {
    storeService.findStoreById(storeId);
    return menuRepository.findMenusByStoreId(storeId);
  }

  /**
   * 메뉴의 옵션 리스트 조회, 옵션은 그룹에 속해있다.
   *
   * @Param menuId 메뉴 아이디
   * @Return groups
   */
  public List<Group> findOptionsWithGroup(long menuId) {
    verifyExistingMenu(menuId);
    return groupRepository.findAllByMenuId(menuId);
  }

  /**
   * 메뉴가 존재하는지 확인, 없는 메뉴이면 예외
   *
   * @throws BusinessLogicException when menu not found
   * @Param menuId 메뉴 아이디
   */
  private void verifyExistingMenu(long menuId) {
    if (!menuRepository.existsByMenuId(menuId)) {
      throw new BusinessLogicException(ExceptionCode.MENU_NOT_FOUND);
    }
  }
}
