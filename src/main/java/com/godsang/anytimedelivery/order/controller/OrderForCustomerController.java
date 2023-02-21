package com.godsang.anytimedelivery.order.controller;

import com.godsang.anytimedelivery.auth.details.UserDetailsImpl;
import com.godsang.anytimedelivery.common.dto.SingleResponseDto;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Validated
public class OrderForCustomerController {
  private final OrderService orderService;
  private final OrderMapper orderMapper;

  @PostMapping
  public ResponseEntity requestOrder(@Valid @RequestBody OrderDto.Post orderPostDto,
                                     @AuthenticationPrincipal UserDetailsImpl principal) {
    Order order = orderMapper.orderDtoToOrder(orderPostDto, principal.getUserId());
    Order response = orderService.request(order);
    return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.CREATED);
  }

  @PatchMapping("/{order-id}")
  public ResponseEntity cancelOrder(@PathVariable("order_id") @Positive Long orderId,
                                    @AuthenticationPrincipal UserDetailsImpl principal) {
    orderService.cancel(orderId, principal.getUserId());
    return new ResponseEntity(HttpStatus.OK);
  }
}
