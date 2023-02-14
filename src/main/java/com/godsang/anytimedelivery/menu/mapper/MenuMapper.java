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
  Option optionDtoToOption(OptionDto optionDto);

  List<Option> optionDtosToOptions(List<OptionDto> optionDtos);

  Group groupDtoToGroup(GroupDto.Post groupDto);

  Menu menuDtoToMenu(MenuDto.Post menuDto);

  OptionDto optionToOptionDto(Option option);

  List<OptionDto> optionsToOptionDtos(List<Option> options);

  GroupDto.Response groupToGroupDto(Group group);

  MenuDto.Response menuToMenuDto(Menu menu);
}
