package com.godsang.anytimedelivery.menu.controller;

import com.godsang.anytimedelivery.common.dto.MultiResponseDto;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.mapper.MenuMapper;
import com.godsang.anytimedelivery.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/categories/{category-id}/stores/{store-id}/menu")
@RequiredArgsConstructor
@Validated
public class MenuForCustomerController {
  private final MenuService menuService;
  private final MenuMapper menuMapper;

  @GetMapping
  public ResponseEntity getMenus(@PathVariable("category-id") @Positive long categoryId,
                                 @PathVariable("store-id") @Positive long storeId) {
    List<Menu> menus = menuService.findStoreMenus(storeId);
    return new ResponseEntity<>(new MultiResponseDto<>(menuMapper.menusToMenuDtos(menus)), HttpStatus.OK);
  }

  @GetMapping("/{menu-id}")
  public ResponseEntity getGroups(@PathVariable("category-id") @Positive long categoryId,
                                  @PathVariable("store-id") @Positive long storeId,
                                  @PathVariable("menu-id") @Positive long menuId) {
    List<Group> groups = menuService.findOptionsWithGroup(menuId);
    return new ResponseEntity<>(new MultiResponseDto<>(menuMapper.groupsToGroupDtos(groups)), HttpStatus.OK);
  }
}
