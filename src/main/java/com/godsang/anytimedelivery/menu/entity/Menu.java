package com.godsang.anytimedelivery.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godsang.anytimedelivery.store.entity.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int price;
  private String description;
  private String photo;
  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Store store;
  @JsonIgnore
  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Group> groups = new ArrayList<>();

  public Menu(Long menuId) {
    this.menuId = menuId;
  }

  @Builder
  private Menu(String name, int price, String description, String photo, List<Group> groups) {
    this.name = name;
    this.price = price;
    this.description = description;
    this.photo = photo;
    this.groups = groups;
  }
}
