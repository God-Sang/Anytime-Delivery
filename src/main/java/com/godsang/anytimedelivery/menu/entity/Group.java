package com.godsang.anytimedelivery.menu.entity;

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

  public void addOptionGroup(OptionGroup optionGroup) {
    this.optionGroups.add(optionGroup);
  }

  @Builder
  private Group(String title, String choiceType) {
    this.title = title;
    this.choiceType = choiceType;
  }
}
