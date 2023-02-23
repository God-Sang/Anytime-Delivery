package com.godsang.anytimedelivery.common.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class ErrorResponse {
  private int status;
  private String message;
  private List<FieldError> fieldErrors;
  private List<ConstraintViolationError> violationErrors;

  private ErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  private ErrorResponse(List<FieldError> fieldErrors,
                        List<ConstraintViolationError> violationErrors) {
    this.fieldErrors = fieldErrors;
    this.violationErrors = violationErrors;
  }

  public static ErrorResponse of(BindingResult bindingResult) {
    return new ErrorResponse(FieldError.of(bindingResult), null);
  }

  public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
    return new ErrorResponse(null, ConstraintViolationError.of(violations));
  }

  public static ErrorResponse of(BusinessLogicException e) {
    return new ErrorResponse(e.getExceptionCode().getCode(), e.getExceptionCode().getDescription());
  }

  public static ErrorResponse of(HttpStatus httpStatus, String message) {
    return new ErrorResponse(httpStatus.value(), message);
  }

  /**
   * Enum Mapping 실패의 예외 메시지
   *
   * @param field
   * @param enumValues
   * @return 예외 정보
   */
  public static ErrorResponse of(String field, Object[] enumValues) {
    StringBuilder sb = new StringBuilder();
    sb.append("A value of '");
    sb.append(field);
    sb.append("' should be one of ");
    for (Object enumValue : enumValues) {
      sb.append(enumValue.toString());
      sb.append(", ");
    }
    sb.deleteCharAt(sb.length() - 1);
    return new ErrorResponse(400, sb.toString());
  }

  @Getter
  @AllArgsConstructor
  public static class FieldError {
    private final String field;
    private final Object rejectedValue;
    private final String reason;

    public static List<FieldError> of(BindingResult bindingResult) {
      final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();

      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }

  @Getter
  @AllArgsConstructor
  public static class ConstraintViolationError {
    private final String propertyPath;
    private final Object rejectedValue;
    private final String reason;

    public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations) {
      return constraintViolations.stream()
          .map(constraintViolation -> new ConstraintViolationError(
              constraintViolation.getPropertyPath().toString(),
              constraintViolation.getInvalidValue().toString(),
              constraintViolation.getMessage()
          )).collect(Collectors.toList());
    }
  }
}
