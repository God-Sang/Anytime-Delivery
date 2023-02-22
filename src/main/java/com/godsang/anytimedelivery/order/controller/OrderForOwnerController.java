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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/owner/stores/{store-id}/orders")
@RequiredArgsConstructor
@Validated
public class OrderForOwnerController {
  private final OrderService orderService;
  private final OrderMapper orderMapper;

  @GetMapping
  public ResponseEntity retrieveOrders(@PathVariable("store-id") @Positive long storeId,
                                       @RequestParam("order-state") OrderStatus orderStatus,
                                       @RequestParam @Positive int page,
                                       @RequestParam @Positive int size,
                                       @AuthenticationPrincipal UserDetailsImpl principal) {
    List<Order> orders = orderService.retrieveOrders(storeId, principal.getUserId(), orderStatus, PageRequest.of(page - 1, size));
    List<OrderDto.ResponseForList> responses = orderMapper.ordersToResponses(orders);
    return new ResponseEntity(new MultiResponseDto(responses), HttpStatus.OK);
  }

  @GetMapping("/{order-id}")
  public ResponseEntity retrieveOrderInDetail(@PathVariable("store-id") @Positive long storeId,
                                              @PathVariable("order-id") @Positive long orderId,
                                              @AuthenticationPrincipal UserDetailsImpl principal) {
    Order order = orderService.retrieveOrder(storeId, principal.getUserId(), orderId);
    OrderDto.Response response = orderMapper.orderToResponse(order);
    return new ResponseEntity(new SingleResponseDto(response), HttpStatus.OK);
  }
}
