package com.godsang.anytimedelivery.menu.controller;

import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import com.godsang.anytimedelivery.menu.dto.GroupDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.mapper.MenuMapper;
import com.godsang.anytimedelivery.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/owner/stores/{store-id}")
@RequiredArgsConstructor
@Validated
public class MenuController {
  private final MenuService menuService;
  private final MenuMapper menuMapper;

  /**
   * 가게의 메뉴를 등록한다.
   */
  @PostMapping
  public ResponseEntity postMenu(@PathVariable("store-id") @Positive long storeId,
                                 @RequestBody @Valid MenuDto.Post menuDto) {
    Menu menu = menuMapper.menuDtoToMenu(menuDto);
    for (GroupDto.Post groupDto : menuDto.getGroups()) {
      Group group = menuMapper.groupDtoToGroup(groupDto);
      menu = menuService.createMenu(storeId, menu, group);
    }
    return new ResponseEntity<>(new SingleResponseDto<>(menuMapper.menuToMenuDto(menu)), HttpStatus.CREATED);
  }
}
