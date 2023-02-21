package com.godsang.anytimedelivery.common.Exception;

import com.godsang.anytimedelivery.order.entity.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  @ExceptionHandler
  public ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    if (e.getRequiredType().equals(OrderStatus.class)) {
      return new ResponseEntity(ErrorResponse.of(e), HttpStatus.BAD_REQUEST);
    }
    throw e;
  }
}
