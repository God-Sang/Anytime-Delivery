package com.godsang.anytimedelivery.menu.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OptionGroup {
  @Id
  private Long optionGroupId;
  @ManyToOne(fetch = FetchType.LAZY)
  private Group group;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Option option;

  public OptionGroup(Group group, Option option) {
    this.group = group;
    this.option = option;
  }
}
