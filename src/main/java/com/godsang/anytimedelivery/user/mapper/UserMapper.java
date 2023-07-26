package com.godsang.anytimedelivery.user.mapper;

import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "role", ignore = true)
  User dtoToUser(UserDto.Post postDto);

  UserDto.Response userToResponse(User user);
}
