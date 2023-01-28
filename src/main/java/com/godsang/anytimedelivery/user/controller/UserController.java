package com.godsang.anytimedelivery.user.controller;

import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.mapper.UserMapper;
import com.godsang.anytimedelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinUser(@RequestBody UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        userService.createUser(user, userDto.getRole());
    }
}
