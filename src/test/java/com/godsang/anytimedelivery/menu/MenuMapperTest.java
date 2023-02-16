package com.godsang.anytimedelivery.menu;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.menu.dto.GroupDto;
import com.godsang.anytimedelivery.menu.dto.MenuDto;
import com.godsang.anytimedelivery.menu.dto.OptionDto;
import com.godsang.anytimedelivery.menu.entity.ChoiceType;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import com.godsang.anytimedelivery.menu.mapper.MenuMapper;
import com.godsang.anytimedelivery.menu.mapper.MenuMapperImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MenuMapperTest {
  private final MenuMapper menuMapper = new MenuMapperImpl();

  @Test
  void optionDtoToOptionTest() {
    //given
    OptionDto.Post optionPostDto = StubData.MockMenuPost.getOptionDto("착한맛", 1000);

    //when
    Option option = menuMapper.optionDtoToOption(optionPostDto);

    //then
    assertThat(option.getName()).isEqualTo(optionPostDto.getName());
  }

  @Test
  void optionDtosToOptionsTest() {
    //given
    List<OptionDto.Post> posts = StubData.MockMenuPost.getOptionDtoList();

    //when
    List<Option> options = menuMapper.optionDtosToOptions(posts);

    //then
    assertThat(options.get(9).getName())
        .isEqualTo(posts.get(9).getName());
  }

  @Test
  void groupDtoToGroupTest() {
    //given
    List<OptionDto.Post> optionPostDtos = StubData.MockMenuPost.getOptionDtoList();
    GroupDto.Post post = StubData.MockMenuPost.getGroupDto("맛 선택", "RADIO", optionPostDtos);

    //when
    Group group = menuMapper.groupDtoToGroup(post);

    //then
    assertThat(group.getOptions().get(9).getName())
        .isEqualTo(optionPostDtos.get(9).getName());
    assertThat(group.getTitle())
        .isEqualTo(post.getTitle());
  }

  @Test
  void groupDtosToGroupsTest() {
    //given
    List<GroupDto.Post> posts = StubData.MockMenuPost.getGroupDtoList();

    //when
    List<Group> groups = menuMapper.groupDtosToGroups(posts);

    //then
    assertThat(groups.get(4).getTitle()).isEqualTo(posts.get(4).getTitle());
    assertThat(groups.get(4).getOptions().get(9).getName())
        .isEqualTo(posts.get(4).getOptions().get(9).getName());
  }

  @Test
  void menuDtoToMenuTest() {
    //given
    List<GroupDto.Post> groupDtos = StubData.MockMenuPost.getGroupDtoList();
    MenuDto.Post post = StubData.MockMenuPost.getMenuDto("떡볶이", 10000, groupDtos);

    //when
    Menu menu = menuMapper.menuDtoToMenu(post);

    //then
    assertThat(menu.getName()).isEqualTo(post.getName());
    assertThat(menu.getGroups().get(4).getTitle()).isEqualTo(groupDtos.get(4).getTitle());
    assertThat(menu.getGroups().get(4).getOptions().get(9).getName())
        .isEqualTo(groupDtos.get(4).getOptions().get(9).getName());
  }

  @Test
  void optionToOptionDtoTest() {
    //given
    Option option = StubData.MockMenu.getOption("매운맛", 1000);

    //when
    OptionDto.Response response = menuMapper.optionToOptionDto(option);

    //then
    assertThat(response.getName()).isEqualTo(option.getName());
  }

  @Test
  void optionsToOptionDtosTest() {
    //given
    Group group = StubData.MockMenu.getMockGroupEntity("맛 선택", ChoiceType.RADIO);
    List<Option> options = StubData.MockMenu.getOptionList(group);

    //when
    List<OptionDto.Response> responses = menuMapper.optionsToOptionDtos(options);

    //when
    assertThat(responses.get(9).getName())
        .isEqualTo(options.get(9).getName());
  }

  @Test
  void groupToGroupDtoTest() {
    //given
    Menu menu = StubData.MockMenu.getMockMenuEntity("떡볶이", 10000);
    Group group = StubData.MockMenu.getMockGroupEntity("맛 선택", ChoiceType.RADIO);
    group.setMenu(menu);
    group.setOptions(StubData.MockMenu.getOptionList(group));

    //when
    GroupDto.Response response = menuMapper.groupToGroupDto(group);

    //then
    assertThat(response.getTitle()).isEqualTo(group.getTitle());
    assertThat(response.getOptions().get(9).getName())
        .isEqualTo(group.getOptions().get(9).getName());
  }

  @Test
  void groupsToGroupDtosTest() {
    //given
    Menu menu = StubData.MockMenu.getMockMenuEntity("떡볶이", 10000);
    List<Group> groups = StubData.MockMenu.getGroupList(menu);

    //when
    List<GroupDto.Response> responses = menuMapper.groupsToGroupDtos(groups);

    //then
    assertThat(responses.get(4).getTitle()).isEqualTo(groups.get(4).getTitle());
    assertThat(responses.get(4).getOptions().get(9).getName())
        .isEqualTo(groups.get(4).getOptions().get(9).getName());
  }

  @Test
  void menuToMenuDtoTest() {
    //given
    Menu menu = StubData.MockMenu.getMockMenuEntity();

    //when
    MenuDto.Response response = menuMapper.menuToMenuDto(menu);

    //then
    assertThat(response.getName()).isEqualTo(menu.getName());
    assertThat(response.getGroups().get(4).getTitle()).isEqualTo(menu.getGroups().get(4).getTitle());
    assertThat(response.getGroups().get(4).getOptions().get(9).getName())
        .isEqualTo(menu.getGroups().get(4).getOptions().get(9).getName());
  }

  @Test
  void bidirectionalMappingTest() {
    //given
    Menu uniDirectionalMenu = menuMapper.menuDtoToMenu(StubData.MockMenuPost.getMenuDto());

    //when
    Menu bidirectionalMenu = menuMapper.bidirectionalMapping(uniDirectionalMenu);

    //then
    assertThat(bidirectionalMenu.getGroups().get(0).getMenu()).isNotNull();
    assertThat(bidirectionalMenu.getGroups().get(0).getOptions().get(0).getGroup()).isNotNull();
  }

}
