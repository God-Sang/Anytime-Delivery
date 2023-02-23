package com.godsang.anytimedelivery.order;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.repository.MenuRepository;
import com.godsang.anytimedelivery.order.entity.Order;
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
import org.springframework.data.domain.PageRequest;

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
  @Autowired
  private DeliveryAreaRepository deliveryAreaRepository;
  private Store store;
  private Menu menu;
  private User user;
  private int numberOfOrders = 10;
  private int numberOfGroups = 2;
  private int numberOfOptions = 3;
  private int numBerOfMenus = 2;

  @BeforeAll
  void init() {
    store = storeRepository.save(StubData.MockStore.getMockEntity());
    menu = menuRepository.save(StubData.MockMenu.getMockMenu(store));
    user = saveUser();
    for (int i = 0; i < numberOfOrders; i++) {
      saveOrder();
    }
  }

  @Test
  @DisplayName("외래키만 가지고 생성한 엔티티가 잘 저장이 되는지")
  void saveTest() {
    Order orderRetrieved = orderRepository.findById(1L).get();
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

  @Test
  @DisplayName("storeId, orderStatus, paging 으로 조회")
  void findAllByStoreAndOrderStatusTest() {
    //when
    List<Order> orders = orderRepository.findAllByStoreAndStatus(store, OrderStatus.ACCEPTED, PageRequest.of(0, 5));

    //then
    assertThat(orders.size()).isEqualTo(5);
    assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.ACCEPTED);
  }


  @Test
  @DisplayName("고객의 주문 리스트 조회")
  void findAllByUserTest() {
    //given
    int size = 5;
    int page = 0;
    //when
    List<Order> orders = orderRepository.findAllByUser(user, PageRequest.of(page, size));

    //then
    assertThat(orders.size()).isEqualTo(size);
  }

  private User saveUser() {
    user = userRepository.save(StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER));
    DeliveryArea deliveryArea = deliveryAreaRepository.save(new DeliveryArea("서울시 요기구 저기동"));
    Address address = StubData.MockAddress.getMockAddress(deliveryArea);
    user.setAddress(address);
    return userRepository.save(user);
  }

  private Order saveOrder() {
    Order order = StubData.MockOrder.getMockOrder(
        user.getUserId(), store.getStoreId(), menu.getMenuId(), menu.getGroups().get(0).getGroupId(),
        menu.getGroups().get(0).getOptions().get(0).getOptionId(),
        numBerOfMenus, numberOfGroups, numberOfOptions);
    return orderRepository.save(order);
  }
}
