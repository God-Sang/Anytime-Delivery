package com.godsang.anytimedelivery.order.controller;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.common.dto.MultiResponseDto;
import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/orders")
@Validated
public class OrderForCustomerController {
  private final OrderService orderService;
  private final OrderMapper orderMapper;

  /**
   * 주문을 요청합니다. 주문상태 (WAIT, ACCEPTED, DELIVERED, CANCELED) 중 WAIT로 저장됩니다.
   *
   * @param orderPostDto
   * @param principal
   */
  @PostMapping
  public ResponseEntity requestOrder(@Valid @RequestBody OrderDto.Post orderPostDto,
                                     @AuthenticationPrincipal UserDetailsImpl principal) {
    Order order = orderMapper.orderDtoToOrder(orderPostDto, principal.getUserId());
    orderService.request(order);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  /**
   * 주문을 취소합니다. 주문상태가 WAIT인 경우에만 취소할 수 있습니다.
   * @param orderId
   * @param principal
   * @return
   */
  @PatchMapping("/{order-id}")
  public ResponseEntity cancelOrder(@PathVariable("order-id") @Positive Long orderId,
                                    @AuthenticationPrincipal UserDetailsImpl principal) {
    orderService.cancelFromCustomer(orderId, principal.getUserId());
    return new ResponseEntity(HttpStatus.OK);
  }

  /**
   * 상세 주문 조회
   *
   * @param orderId
   * @param principal
   * @return 주문한 가게, 메뉴, 옵션 정보를 포함한 주문 정보
   */
  @GetMapping("/{order-id}")
  public ResponseEntity retrieveOrder(@PathVariable("order-id") @Positive Long orderId,
                                 @AuthenticationPrincipal UserDetailsImpl principal) {
    Order order = orderService.retrieveOrderOfCustomer(orderId, principal.getUserId());
    OrderDto.Response response = orderMapper.orderToResponse(order);
    return new ResponseEntity(new SingleResponseDto(response), HttpStatus.OK);
  }

  /**
   * 주문 리스트 조회
   *
   * @param principal
   * @param page
   * @param size
   * @return 주문 리스트
   */

  @GetMapping
  public ResponseEntity retrieveOrders(@AuthenticationPrincipal UserDetailsImpl principal,
                                       @RequestParam @Positive int page,
                                       @RequestParam @Positive int size) {
    List<Order> orders = orderService.retrieveOrdersOfCustomer(principal.getUserId(), PageRequest.of(page - 1, size));
    List<OrderDto.ResponseForList> response = orderMapper.ordersToResponses(orders);
    return new ResponseEntity(new MultiResponseDto(response), HttpStatus.OK);
  }
}
