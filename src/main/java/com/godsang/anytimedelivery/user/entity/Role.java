package com.godsang.anytimedelivery.user.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_CUSTOMER("customer"),
    ROLE_OWNER("owner");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
