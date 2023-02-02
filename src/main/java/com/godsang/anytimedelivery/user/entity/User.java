package com.godsang.anytimedelivery.user.entity;

import com.godsang.anytimedelivery.common.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity(name = "USERS")
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true, nullable = false, length = 40)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false, length = 10)
  private String nickName;

  @Column(unique = true, nullable = false, length = 13)
  private String phone;

  @Column(nullable = false, updatable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Builder
  public User(Long userId, String email, String password, String nickName, String phone, Role role) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.phone = phone;
    this.role = role;
  }
}
