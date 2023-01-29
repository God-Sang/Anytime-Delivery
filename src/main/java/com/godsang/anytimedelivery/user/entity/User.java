package com.godsang.anytimedelivery.user.entity;

import com.godsang.anytimedelivery.audit.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "USERS")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String nickName;

    @Column(nullable = false, length = 13)
    private String phone;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
