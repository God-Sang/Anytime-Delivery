package com.godsang.anytimedelivery.common.Exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {
  private final ExceptionCode exceptionCode;

  public BusinessLogicException(ExceptionCode exceptionCode) {
    this.exceptionCode = exceptionCode;
  }
}
