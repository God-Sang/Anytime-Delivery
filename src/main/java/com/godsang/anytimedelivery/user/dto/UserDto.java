package com.godsang.anytimedelivery.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto { // TODO validation
    private String email;
    private String password;
    private String phone;
    private String nickName;
    private Boolean isCustomer;
}
