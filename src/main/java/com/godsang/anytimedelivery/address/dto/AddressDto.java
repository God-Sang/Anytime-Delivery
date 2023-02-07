package com.godsang.anytimedelivery.address.dto;

import lombok.Getter;

import java.util.List;

public class AddressDto {
  @Getter
  public static class SearchResponseDto {
    private Results results;

    @Getter
    public static class Results {
      private Common common;
      private List<Juso> juso;
    }

    @Getter
    public static class Juso {
      private String roadAddr; // 도로명 주소
      private String jibunAddr; // 지번 주소
      private String emdNm; // 읍면동
    }

    @Getter
    public static class Common {
      private String countPerPage;
      private String totalPage;
      private String currentPage;
    }
  }
}
