package com.godsang.anytimedelivery.user.entity;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.common.audit.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS")
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

  @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
  @JoinColumn(name = "address_id")
  private Address address;

  public User(Long userId) {
    this.userId = userId;
  }
  @Builder
  private User(Long userId, String email, String password, String nickName, String phone, Role role) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.phone = phone;
    this.role = role;
  }
}
