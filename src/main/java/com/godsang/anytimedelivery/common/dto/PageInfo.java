package com.godsang.anytimedelivery.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageInfo {
  private int page;
  private int size;
  private long totalElements;
  private int totalPage;
}
