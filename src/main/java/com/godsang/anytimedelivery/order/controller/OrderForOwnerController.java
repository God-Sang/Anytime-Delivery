package com.godsang.anytimedelivery.order.controller;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.common.dto.MultiResponseDto;
import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/owner/stores/{store-id}/orders")
@RequiredArgsConstructor
@Validated
public class OrderForOwnerController {
  private final OrderService orderService;
  private final OrderMapper orderMapper;

  /**
   * 주문 리스트 조회
   *
   * @param storeId
   * @param orderStatus
   * @param page
   * @param size
   * @param principal
   * @return 주문 리스트
   * @throws MethodArgumentTypeMismatchException - 유효하지 않은 OrderStatus를 입력한 경우
   */
  @GetMapping
  public ResponseEntity retrieveOrders(@PathVariable("store-id") @Positive long storeId,
                                       @RequestParam("order-state") OrderStatus orderStatus,
                                       @RequestParam @Positive int page,
                                       @RequestParam @Positive int size,
                                       @AuthenticationPrincipal UserDetailsImpl principal) {
    List<Order> orders = orderService.retrieveOrdersOfStore(storeId, principal.getUserId(), orderStatus, PageRequest.of(page - 1, size));
    List<OrderDto.ResponseForList> responses = orderMapper.ordersToResponses(orders);
    return new ResponseEntity(new MultiResponseDto(responses), HttpStatus.OK);
  }

  /**
   * 주문 상세 조회
   *
   * @param storeId
   * @param orderId
   * @param principal
   * @return 상세 주문
   */
  @GetMapping("/{order-id}")
  public ResponseEntity retrieveOrderInDetail(@PathVariable("store-id") @Positive long storeId,
                                              @PathVariable("order-id") @Positive long orderId,
                                              @AuthenticationPrincipal UserDetailsImpl principal) {
    Order order = orderService.retrieveOrderOfStore(storeId, principal.getUserId(), orderId);
    OrderDto.Response response = orderMapper.orderToResponse(order);
    return new ResponseEntity(new SingleResponseDto(response), HttpStatus.OK);
  }

  /**
   * 주문 상태 변경 (주문 수락, 배송 완료)
   *
   * @param storeId
   * @param orderId
   * @param patch
   * @param principal
   * @throws MethodArgumentNotValidException - 요청한 OrderStatus가 ACCEPTED 또는 DELIVERED가 아닌 경우
   */
  @PatchMapping("/{order-id}")
  public ResponseEntity changeOrderState(@PathVariable("store-id") @Positive long storeId,
                                         @PathVariable("order-id") @Positive long orderId,
                                         @Valid @RequestBody OrderDto.Patch patch,
                                         @AuthenticationPrincipal UserDetailsImpl principal) {
    orderService.changeOrderStatus(storeId, orderId, principal.getUserId(), patch.getOrderStatus());
    return new ResponseEntity(HttpStatus.OK);
  }

  /**
   * 주문 취소
   *
   * @param storeId
   * @param orderId
   * @param patchCancel
   * @param principal
   * @throws MethodArgumentNotValidException - 주문 사유가 없는 경우
   */
  @PatchMapping("/{order-id}/cancel")
  public ResponseEntity cancelOrder(@PathVariable("store-id") @Positive long storeId,
                                    @PathVariable("order-id") @Positive long orderId,
                                    @Valid @RequestBody OrderDto.PatchCancel patchCancel,
                                    @AuthenticationPrincipal UserDetailsImpl principal) {
    orderService.cancelFromOwner(orderId, storeId, principal.getUserId(), patchCancel.getReason());
    return new ResponseEntity(HttpStatus.OK);
  }
}
