package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.repository.OrderInMemoryRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderInMemoryRepositoryTest {
  @Autowired
  private OrderInMemoryRepository repository;

  @Test
  void SerializeTest() {
    User user = StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER);
    Store store = StubData.MockStore.getMockEntity();
    Order order = StubData.MockOrder.getMockOrder(store, user, OrderStatus.IN_CART);

    repository.save(1L, order);
    Order retrievedOrder = (Order) repository.retrieve(1L);
  }
}
