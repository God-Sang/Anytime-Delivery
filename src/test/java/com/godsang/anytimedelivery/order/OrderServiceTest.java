package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
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
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {
  @Autowired
  private OrderService orderService;
  @Autowired
  private OrderMapper orderMapper;
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
      orderService.request(order);
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
      orderService.request(order);
    });
  }

  @Test
  @DisplayName("주문 취소 성공")
  void cancelTest() {
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.WAIT, 1L);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    assertDoesNotThrow(() -> orderService.cancelFromCustomer(1L, 1L));
  }

  @Test
  @DisplayName("주문 취소 예외 : 주문의 주인이 유저가 아닌 경우")
  void cancelExceptionTest_NotOwnerOfOrder() {
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.ACCEPTED, 1L);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    assertThrows(BusinessLogicException.class, () -> orderService.cancelFromCustomer(1L, 999L));
  }

  @Test
  @DisplayName("주문 취소 예외 : 이미 배달 완료된 주문인 경우")
  void cancelExceptionTest_AlreadyDelivered() {
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.DELIVERED, 1L);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    assertThrows(BusinessLogicException.class, () -> orderService.cancelFromCustomer(1L, 1L));
  }

  @Test
  @DisplayName("주문 리스트 조회")
  void retrieveOrdersTest() {
    //given
    Store store = StubData.MockStore.getMockEntity();
    store.setUser(StubData.MockUser.getMockEntity(Role.ROLE_OWNER));
    given(storeService.findStoreById(anyLong())).willReturn(store);
    given(orderRepository.findAllByStoreAndStatus(any(), any(), any()))
        .willReturn(List.of(StubData.MockOrder.getMockOrder(OrderStatus.ACCEPTED, 1L)));

    //when
    assertDoesNotThrow(() -> {
      List<Order> orders = orderService.retrieveOrdersOfStore(1L, 1L, OrderStatus.ACCEPTED, PageRequest.of(0, 3));
    });
  }

  @Test
  @DisplayName("고객의 주문 조회 : 정상")
  void retrieveOrderOfCustomerTest() {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    Order mockOrder = mock(Order.class);
    User mockUser = mock(User.class);
    when(mockOrder.getUser()).thenReturn(mockUser);
    when(mockUser.getUserId()).thenReturn(userId);
    given(orderRepository.findByOrderId(anyLong())).willReturn(Optional.of(mockOrder));

    //when
    assertDoesNotThrow(() -> orderService.retrieveOrderOfCustomer(orderId, userId));
  }

  @Test
  @DisplayName("고객의 주문 조회 : 고객의 주문이 아닌 경우")
  void retrieveOrderOfCustomerExceptionTest1() {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    Long wrongUserId = userId + 1L;
    Order mockOrder = mock(Order.class);
    User mockUser = mock(User.class);
    when(mockOrder.getUser()).thenReturn(mockUser);
    when(mockUser.getUserId()).thenReturn(wrongUserId);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(mockOrder));

    //when
    assertThrows(BusinessLogicException.class, () -> orderService.retrieveOrderOfCustomer(orderId, userId));
  }

  @Test
  @DisplayName("고객의 주문 조회 : 없는 주문일 경우")
  void retrieveOrderOfCustomerExceptionTest2() {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    assertThrows(BusinessLogicException.class, () -> orderService.retrieveOrderOfCustomer(orderId, userId));
  }

  @Test
  @DisplayName("사장의 주문 취소")
  void cancelFromOwnerTest() {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    Long storeId = 1L;
    String reason = "재료 소진";
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.WAIT, orderId);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    //when
    assertDoesNotThrow(() -> orderService.cancelFromOwner(orderId, storeId, userId, reason));
  }

  @Test
  @DisplayName("사장의 주문 취소 예외 : 존재하지 않는 주문일 경우")
  void cancelFromOwnerExceptionTest1() {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    Long storeId = 1L;
    String reason = "재료 소진";
    given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

    //when
    assertThrows(BusinessLogicException.class,
        () -> orderService.cancelFromOwner(orderId, storeId, userId, reason));
  }

  @Test
  @DisplayName("사장의 주문 취소 예외 : 가게에 요청된 주문이 아닐 경우")
  void cancelFromOwnerExceptionTest2() {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    Long storeId = 999L;
    String reason = "재료 소진";
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.WAIT, orderId);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    //when
    assertThrows(BusinessLogicException.class,
        () -> orderService.cancelFromOwner(orderId, storeId, userId, reason));
  }

  @ParameterizedTest
  @ValueSource(strings = {"ACCEPTED", "DELIVERED", "CANCELED"})
  @DisplayName("사장의 주문 취소 예외 : 주문상태가 WAIT가 아닐 경우")
  void cancelFromOwnerExceptionTest3(String orderStatus) {
    // given
    Long userId = 1L;
    Long orderId = 1L;
    Long storeId = 1L;
    String reason = "재료 소진";
    Order order = StubData.MockOrder.getMockOrder(OrderStatus.valueOf(orderStatus), orderId);
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

    //when
    assertThrows(BusinessLogicException.class,
        () -> orderService.cancelFromOwner(orderId, storeId, userId, reason));
  }
}
