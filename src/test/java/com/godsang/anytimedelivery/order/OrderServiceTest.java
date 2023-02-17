package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.service.DeliveryAreaService;
import com.godsang.anytimedelivery.helper.stub.MockDto;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.order.dto.OrderDto;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.mapper.OrderMapper;
import com.godsang.anytimedelivery.order.repository.OrderRepository;
import com.godsang.anytimedelivery.order.service.OrderService;
import com.godsang.anytimedelivery.order.service.PayService;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.service.StoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class OrderServiceTest {
  @Autowired
  private OrderService orderService;
  private OrderMapper orderMapper = new OrderMapper();
  @MockBean
  private OrderRepository orderRepository;
  @MockBean
  private StoreService storeService;
  @MockBean
  private DeliveryAreaService deliveryAreaService;
  @MockBean
  private PayService payService;

  @Test
  @DisplayName("주문 요청 테스트")
  void requestTest() {
    //given
    Store store = StubData.MockStore.getMockEntityWithDeliveryArea();
    given(storeService.findStoreById(anyLong())).willReturn(store);
    given(deliveryAreaService.findUserDeliveryArea(anyLong())).willReturn(store.getDeliveryAreaStores().get(3).getDeliveryArea());

    OrderDto.Post post = MockDto.OrderPost.get();
    Order order = orderMapper.orderDtoToOrder(post, 1L);

    //when
    assertDoesNotThrow(() -> {
      Order savedOrder = orderService.request(order);
    });
  }

  @Test
  @DisplayName("주문 요청 테스트 예외 : 가게가 닫은 경우")
  void requestExceptionTest_NotOpen() {
    //given
    Store store = StubData.MockStore.getMockEntityWithDeliveryArea();
    given(storeService.findStoreById(anyLong())).willReturn(store);
    given(deliveryAreaService.findUserDeliveryArea(anyLong())).willReturn(store.getDeliveryAreaStores().get(3).getDeliveryArea());
    doThrow(new BusinessLogicException(ExceptionCode.STORE_CLOSED)).when(storeService).verifyOpen(any());

    OrderDto.Post post = MockDto.OrderPost.get();
    Order order = orderMapper.orderDtoToOrder(post, 1L);

    //when
    assertThrows(BusinessLogicException.class, () -> {
      Order savedOrder = orderService.request(order);
    });
  }

  @Test
  @DisplayName("주문 요청 테스트 예외 : 배달 가능 지역이 아닌 경우")
  void requestExceptionTest_NotDeliveryArea() {
    //given
    Store store = StubData.MockStore.getMockEntityWithDeliveryArea();
    given(storeService.findStoreById(anyLong())).willReturn(store);
    given(deliveryAreaService.findUserDeliveryArea(anyLong())).willReturn(new DeliveryArea(1111L, "주소"));

    OrderDto.Post post = MockDto.OrderPost.get();
    Order order = orderMapper.orderDtoToOrder(post, 1L);

    //when
    assertThrows(BusinessLogicException.class, () -> {
      Order savedOrder = orderService.request(order);
    });
  }

  @Test
  @DisplayName("주문 취소 성공")
  void cancelTest() {
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.ACCEPTED);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    assertDoesNotThrow(() -> orderService.cancel(1L, 1L));
  }

  @Test
  @DisplayName("주문 취소 예외 : 주문의 주인이 유저가 아닌 경우")
  void cancelExceptionTest_NotOwnerOfOrder() {
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.ACCEPTED);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    assertThrows(BusinessLogicException.class,
        () -> orderService.cancel(1L, 999L));
  }

  @Test
  @DisplayName("주문 취소 예외 : 이미 배달 완료된 주문인 경우")
  void cancelExceptionTest_AlreadyDelivered() {
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.DELIVERED);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    assertThrows(BusinessLogicException.class,
        () -> orderService.cancel(1L, 1L));
  }
}
