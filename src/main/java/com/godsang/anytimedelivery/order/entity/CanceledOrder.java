package com.godsang.anytimedelivery.order.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CanceledOrder {
  private final String CUSTOMER_REQUESTED_CANCEL = "고객 요청";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long canceledOrderId;
  private String reason;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  /**
   * 사장의 주문 취소
   * 사유를 필수로 받아서 생성합니다.
   *
   * @param reason
   * @param order
   */
  public CanceledOrder(String reason, Order order) {
    this.reason = reason;
    this.order = order;
  }

  /**
   * 고객의 주문 취소
   * 사유가 없기 때문에 따로 입력 받지 않고, 미리 정의된 사유를 입력합니다.
   * @param order
   */
  public CanceledOrder(Order order) {
    this.reason = CUSTOMER_REQUESTED_CANCEL;
    this.order = order;
  }
}
