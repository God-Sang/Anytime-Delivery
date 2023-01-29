package com.godsang.anytimedelivery.category.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Categories for stores. Only an admin can manage it.
 */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
  private Long categoryId;
  private String name;

  protected void changeName(String name) {
    this.name = name;
  }
}
