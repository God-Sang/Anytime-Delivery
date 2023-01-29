package com.godsang.anytimedelivery.user.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER"),
    ROLE_OWNER("OWNER");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
