package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderGroup;
import com.godsang.anytimedelivery.order.entity.OrderMenu;
import com.godsang.anytimedelivery.order.entity.OrderOption;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.order.repository.OrderRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class OrderRepositoryTest {
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private UserRepository userRepository;
  private Store store;
  private Menu menu;
  private User user;
  private int numberOfOptions = 3;
  private int numberOfGroups = 3;

  @BeforeAll
  void init() {
    store = storeRepository.save(StubData.MockStore.getMockEntity());
    menu = menuRepository.save(StubData.MockMenu.getMockMenu(store));
    user = userRepository.save(StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER));
  }

  @Test
  @DisplayName("연관 관계 매핑 테스트")
  void test() {
    Order order = StubData.MockOrder.getMockOrder(store, user, OrderStatus.IN_CART);
    OrderMenu orderMenu = StubData.MockOrder.getMockOrderMenu(menu, order, 1);
    order.addFoodTotalPrice(menu.getPrice());

    for (int i = 0; i < numberOfGroups; i++) {
      OrderGroup orderGroup = StubData.MockOrder.getMockOrderGroup(menu.getGroups().get(i), orderMenu);

      for (int j = 0; j < numberOfOptions; j++) {
        OrderOption option = StubData.MockOrder.getMockOrderOption(menu.getGroups().get(i).getOptions().get(j), orderGroup);
        orderGroup.addOrderOption(option);
        order.addFoodTotalPrice(menu.getGroups().get(i).getOptions().get(j).getPrice());
      }
      orderMenu.addOrderGroup(orderGroup);
    }

    order.addOrderMenu(orderMenu);

    orderRepository.save(order);
    List<Order> orders = orderRepository.findAllByStore(store);
    assertThat(orders.size()).isEqualTo(1);
    assertThat(orders.get(0).getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().size())
        .isEqualTo(numberOfOptions);
  }

}
