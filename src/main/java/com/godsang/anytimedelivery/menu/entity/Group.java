package com.godsang.anytimedelivery.menu.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OPTION_GROUP")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Long groupId;
  @Column(nullable = false)
  private String title;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChoiceType choiceType;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Menu menu;
  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Option> options = new ArrayList<>();

  public Group(Long groupId) {
    this.groupId = groupId;
  }
  @Builder
  private Group(String title, ChoiceType choiceType, List<Option> options) {
    this.title = title;
    this.choiceType = choiceType;
    this.options = options;
  }
}
