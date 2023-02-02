package com.godsang.anytimedelivery.user.controller;

import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.mapper.UserMapper;
import com.godsang.anytimedelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping("/signup")
  public ResponseEntity signupUser(@Valid @RequestBody UserDto.Post postDto) {
    User user = userService.createUser(userMapper.dtoToUser(postDto), postDto.getRole());

    return new ResponseEntity(new SingleResponseDto<>(userMapper.userToResponse(user)), HttpStatus.CREATED);
  }
}
