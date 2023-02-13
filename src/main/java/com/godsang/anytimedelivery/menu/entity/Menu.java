package com.godsang.anytimedelivery.menu.entity;

import com.godsang.anytimedelivery.store.entity.Store;
import lombok.Getter;
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
@Entity
public class Menu {
  @Id
  private Long menuId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private int price;
  private String photo;
  @ManyToOne(fetch = FetchType.LAZY)
  private Menu menu;
  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Menu> option = new ArrayList<>();
  @ManyToOne(fetch = FetchType.LAZY)
  private Store store;
  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Group> groups = new ArrayList<>();
}
