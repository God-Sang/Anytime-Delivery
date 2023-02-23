package com.godsang.anytimedelivery.common.Exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler
  public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    return new ResponseEntity(ErrorResponse.of(e), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
    return new ResponseEntity(ErrorResponse.of(e.getConstraintViolations()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
    return new ResponseEntity(ErrorResponse.of(e), HttpStatus.valueOf(e.getExceptionCode().getCode()));
  }

  /**
   * 컨트롤러 method argument로 resolve하지 못할 경우 발생하는 예외.
   * Enum으로 전환하지 못하는 경우 핸들링
   */
  @ExceptionHandler
  public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    if (e.getRequiredType().isEnum()) {
      return new ResponseEntity(ErrorResponse.of(e.getName(), e.getRequiredType().getEnumConstants()), HttpStatus.BAD_REQUEST);
    }
    throw e;
  }

  /**
   * HttpMessageConverter가 convert에 실패하면 발생하는 예외.
   * Json을 DTO의 필드인 Enum으로 전환하지 못할 경우를 핸들링
   */
  @ExceptionHandler
  public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    InvalidFormatException invalidFormatException;
    try {
      invalidFormatException = (InvalidFormatException) e.getCause();
    } catch (Exception exception) {
      throw e;
    }
    Class type = invalidFormatException.getTargetType();
    if (type.isEnum()) {
      return new ResponseEntity(ErrorResponse.of(invalidFormatException.getPath().get(0).getFieldName(),
          type.getEnumConstants()), HttpStatus.BAD_REQUEST);
    }
    throw e;
  }
}
