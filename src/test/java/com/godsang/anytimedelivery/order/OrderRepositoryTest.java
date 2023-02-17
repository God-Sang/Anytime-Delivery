package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
  private Long orderId;

  @BeforeAll
  void init() {
    store = storeRepository.save(StubData.MockStore.getMockEntity());
    menu = menuRepository.save(StubData.MockMenu.getMockMenu(store));
    user = userRepository.save(StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER));
    Order order = saveOrder();
    orderId = order.getOrderId();
  }

  @Test
  @DisplayName("연관 관계 매핑")
  void test() {
    Order order = StubData.MockOrder.getMockOrder(null, store, user, OrderStatus.ACCEPTED);
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
    assertThat(orders.size()).isEqualTo(2);
    assertThat(orders.get(1).getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().size())
        .isEqualTo(numberOfOptions);
  }


  @Test
  @DisplayName("외래키만 가지고 생성한 엔티티가 잘 저장이 되는지")
  void saveTest() {
    saveOrder();
    Order orderRetrieved = orderRepository.findById(orderId).get();
    assertThat(orderRetrieved.getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().get(0).getOrderOptionId())
        .isNotNull();
  }

  @Test
  @DisplayName("그래프 탐색을 통한 정보 조회")
  void graphSearchTest() {
    List<Order> orders = orderRepository.findAll();
    Order order = orderRepository.findAll().get(0);

    String storeName = order.getStore().getName();
    String menuName = order.getOrderMenus().get(0).getMenu().getName();
    Integer menuPrice = order.getOrderMenus().get(0).getMenu().getPrice();
    String groupTitle = order.getOrderMenus().get(0).getOrderGroups().get(0).getGroup().getTitle();
    String optionName = order.getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().get(0).getOption().getName();
    Integer optionPrice = order.getOrderMenus().get(0).getOrderGroups().get(0).getOrderOptions().get(0).getOption().getPrice();

    assertThat(storeName).isNotNull();
    assertThat(menuName).isNotNull();
    assertThat(menuPrice).isNotNull();
    assertThat(groupTitle).isNotNull();
    assertThat(optionName).isNotNull();
    assertThat(optionPrice).isNotNull();
  }

  private Order saveOrder() {
    Order order = Order.builder()
        .status(OrderStatus.ACCEPTED)
        .user(new User(user.getUserId()))
        .request("맛있게 해주세요~")
        .store(new Store(store.getStoreId()))
        .build();

    OrderMenu orderMenu = OrderMenu.builder()
        .menu(new Menu(menu.getMenuId()))
        .order(order)
        .amount(1)
        .build();

    OrderGroup orderGroup = OrderGroup.builder()
        .group(new Group(menu.getGroups().get(0).getGroupId()))
        .orderMenu(orderMenu)
        .build();

    OrderOption orderOption = OrderOption.builder()
        .option(new Option(menu.getGroups().get(0).getOptions().get(0).getOptionId()))
        .orderGroup(orderGroup)
        .build();

    orderGroup.addOrderOption(orderOption);
    orderMenu.getOrderGroups().add(orderGroup);
    order.getOrderMenus().add(orderMenu);

    return orderRepository.save(order);
  }
}
