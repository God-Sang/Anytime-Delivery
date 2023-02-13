package com.godsang.anytimedelivery.menu.entity;

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
public class Group {
  @Id
  private Long groupId;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String choiceType;
  @ManyToOne(fetch = FetchType.LAZY)
  private Menu menu;
  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
  private List<OptionGroup> optionGroups = new ArrayList<>();
}
