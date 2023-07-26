package com.godsang.anytimedelivery.menu.mapper;

import com.godsang.anytimedelivery.menu.dto.GroupDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.dto.OptionDto;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {
  Option optionDtoToOption(OptionDto.Post optionDto);

  List<Option> optionDtosToOptions(List<OptionDto.Post> optionDtos);

  Group groupDtoToGroup(GroupDto.Post groupDto);

  List<Group> groupDtosToGroups(List<GroupDto.Post> groupDtos);

  Menu menuDtoToMenu(MenuDto.Post menuDto);

  OptionDto.Response optionToOptionDto(Option option);

  List<OptionDto.Response> optionsToOptionDtos(List<Option> options);

  GroupDto.Response groupToGroupDto(Group group);

  List<GroupDto.Response> groupsToGroupDtos(List<Group> group);

  MenuDto.Response menuToMenuDto(Menu menu);

  default Menu bidirectionalMapping(Menu menu) {
    for (Group group : menu.getGroups()) {
      group.setMenu(menu);
      for (Option option : group.getOptions()) {
        option.setGroup(group);
      }
    }
    return menu;
  }
  List<MenuDto.Response> menusToMenuDtos(List<Menu> menus);
}
