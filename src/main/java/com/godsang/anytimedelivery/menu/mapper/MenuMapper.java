package com.godsang.anytimedelivery.menu.mapper;

import com.godsang.anytimedelivery.menu.dto.common.GroupDto;
import com.godsang.anytimedelivery.menu.dto.common.MenuDto;
import com.godsang.anytimedelivery.menu.dto.common.ParentMenuDto;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {
  Menu menuDtoToOption(MenuDto menuDto);

  List<Menu> menuDtosToOptions(List<MenuDto> menuDtos);

  Group groupDtoToGroup(GroupDto groupDto);

  List<Group> groupDtosToGroups(List<GroupDto> groupDtos);

  Menu parentMenuDtoToMenu(ParentMenuDto parentMenuDto);

  List<Menu> parentMenuDtosToMenus(List<ParentMenuDto> parentMenuDtos);

  MenuDto optionToMenuDto(Menu menu);

  List<MenuDto> optionsToMenuDtos(List<Menu> menus);

  GroupDto groupToGroupDto(Group group, List<MenuDto> option);

  List<GroupDto> groupsToGroupDtos(List<Group> groups);

  ParentMenuDto menuToParentMenuDto(Menu menu, List<Group> group);

  List<ParentMenuDto> menusToParentMenuDtos(List<Menu> menus);
}
