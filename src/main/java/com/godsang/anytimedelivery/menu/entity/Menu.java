package com.godsang.anytimedelivery.menu.entity;

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
  private Long menuId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int price;
  private String description;
  private String photo;
  @ManyToOne(fetch = FetchType.LAZY)
  private Store store;
  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Group> groups = new ArrayList<>();

  public void addGroup(Group group) {
    this.groups.add(group);
  }

  @Builder
  private Menu(String name, int price, String description, String photo) {
    this.name = name;
    this.price = price;
    this.description = description;
    this.photo = photo;
  }
}
