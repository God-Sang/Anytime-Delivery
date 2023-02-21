package com.godsang.anytimedelivery.helper.stub;


import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.menu.entity.ChoiceType;
import com.godsang.anytimedelivery.menu.entity.Group;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.menu.entity.Option;
import com.godsang.anytimedelivery.order.entity.Order;
import com.godsang.anytimedelivery.order.entity.OrderGroup;
import com.godsang.anytimedelivery.order.entity.OrderMenu;
import com.godsang.anytimedelivery.order.entity.OrderOption;
import com.godsang.anytimedelivery.order.entity.OrderStatus;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StubData {
  public static class MockUser {
    public static User getMockEntity(long userId, String email, String phone, String nickName) {
      return User.builder()
          .userId(userId)
          .email(email)
          .password("1q2w3e4r@")
          .phone(phone)
          .nickName(nickName)
          .role(Role.ROLE_CUSTOMER)
          .build();
    }

    public static User getMockEntity(Role role) {
      return User.builder()
          .userId(1L)
          .email("anytime@email.com")
          .phone("010-1234-5678")
          .nickName("애니타임")
          .password("1q2w3e4r@")
          .role(role)
          .build();
    }

    public static User getMockEntity() {
      User user = StubData.MockUser.getMockEntity(Role.ROLE_CUSTOMER);
      DeliveryArea deliveryArea = new DeliveryArea("서울시 요기구 저기동");
      Address address = MockAddress.getMockAddress(deliveryArea);
      user.setAddress(address);
      return user;
    }
  }

  public static class MockStore {
    public static Store getMockEntity(Long storeId, String registrationNumber, String name, String tel, String address) {
      return Store.builder()
          .storeId(storeId)
          .registrationNumber(registrationNumber)
          .name(name)
          .tel(tel)
          .address(address)
          .openTime(LocalTime.now())
          .closeTime(LocalTime.now())
          .deliveryFee(6000)
          .deliveryTime(30)
          .build();
    }

    public static Store getMockEntity() {
      return Store.builder()
          .storeId(1L)
          .registrationNumber("123-12-12345")
          .name("애니타임 치킨")
          .tel("02-123-1234")
          .address("경기도 성남시 분당구 123")
          .openTime(LocalTime.of(9, 30))
          .closeTime(LocalTime.of(21, 30))
          .deliveryFee(1000)
          .deliveryTime(30)
          .build();
    }

    public static Store getMockEntityWithDeliveryArea() {
      Store store = getMockEntity();
      List<DeliveryAreaStore> deliveryAreaStores = new ArrayList<>();
      for (int i = 1; i <= 5; i++) {
        DeliveryArea deliveryArea = new DeliveryArea((long) i, "서울시 저쪽구 이쪽" + i + "동");
        DeliveryAreaStore deliveryAreaStore = new DeliveryAreaStore(store, deliveryArea);
        deliveryAreaStores.add(deliveryAreaStore);
      }
      store.setDeliveryAreaStores(deliveryAreaStores);
      return store;
    }
  }

  public static class MockAddress {
    public static Address getMockAddress(String address, String detailAddress) {
      return Address.builder()
          .address(address)
          .detailAddress(detailAddress)
          .build();
    }

    public static Address getMockAddress(DeliveryArea deliveryArea) {
      return Address.builder()
          .address("서울시 요기구 저기동")
          .detailAddress("105동 103호")
          .deliveryArea(deliveryArea)
          .build();
    }
  }

  public static class MockMenu {
    public static Menu getMockMenu(String name, int price) {
      return Menu.builder()
          .name(name)
          .price(price)
          .description("메뉴 설명")
          .photo("사진 경로")
          .build();
    }

    public static Menu getMockMenu() {
      Menu menu = getMockMenu("떡볶이", 10000);
      List<Group> groups = getGroupList(menu);
      menu.setGroups(groups);
      return menu;
    }

    public static Menu getMockMenu(Store store) {
      Menu menu = getMockMenu();
      menu.setStore(store);
      return menu;
    }


    public static Group getMockGroup(String title, ChoiceType choiceType) {
      return Group.builder()
          .title(title)
          .choiceType(choiceType)
          .build();
    }

    public static Option getMockOption(String name, int price) {
      return Option.builder()
          .name(name)
          .price(price)
          .build();
    }

    public static List<Option> getOptionList(Group group) {
      List<Option> options = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        Option option = getMockOption("매운맛-" + i, 1000);
        option.setGroup(group);
        options.add(option);
      }
      return options;
    }

    public static List<Group> getGroupList(Menu menu) {
      List<Group> groups = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        Group group = getMockGroup("맛 선택-" + i, ChoiceType.RADIO);
        List<Option> options = getOptionList(group);
        group.setOptions(options);
        group.setMenu(menu);
        groups.add(group);
      }
      return groups;
    }
  }
  public static class MockOrder {
    public static Order getMockOrder(OrderStatus orderStatus, Long userId, Long storeId, Long menuId, Long groupId, Long optionId,
                                     int numBerOfMenus, int numberOfGroups, int numberOfOptions) {
      Order order = Order.builder()
          .status(orderStatus)
          .user(new User(userId))
          .request("맛있게 해주세요~")
          .store(new Store(storeId))
          .build();

      for (int p = 0; p < numBerOfMenus; p++) {
        OrderMenu orderMenu = OrderMenu.builder()
            .menu(new Menu(menuId))
            .order(order)
            .amount(1)
            .build();
        for (int i = 0; i < numberOfGroups; i++) {
          OrderGroup orderGroup = OrderGroup.builder()
              .group(new Group(groupId))
              .orderMenu(orderMenu)
              .build();
          for (int j = 0; j < numberOfOptions; j++) {
            OrderOption orderOption = OrderOption.builder()
                .option(new Option(optionId))
                .orderGroup(orderGroup)
                .build();
            orderGroup.addOrderOption(orderOption);
          }
          orderMenu.addOrderGroup(orderGroup);
        }
        order.addOrderMenu(orderMenu);
      }
      return order;
    }
    public static Order getMockOrder(OrderStatus orderStatus) {
      Store store = MockStore.getMockEntity();
      User user = MockUser.getMockEntity(Role.ROLE_CUSTOMER);
      Order order = getMockOrder(1L, store, user, orderStatus);
      OrderMenu orderMenu = getMockOrderMenu(MockMenu.getMockMenu(), order, 1);
      OrderGroup orderGroup = getMockOrderGroup(MockMenu.getMockGroup("맛 선택", ChoiceType.CHECK), orderMenu);
      orderGroup.addOrderOption(getMockOrderOption(MockMenu.getMockOption("매운맛", 10000), orderGroup));
      orderMenu.addOrderGroup(orderGroup);
      order.addOrderMenu(orderMenu);
      return order;
    }
    public static Order getMockOrder(Long orderId, Store store, User user, OrderStatus orderStatus) {
      return Order.builder()
          .orderId(orderId)
          .status(orderStatus)
          .store(store)
          .user(user)
          .request("느리게 와주세요")
          .deliveryFee(1000)
          .deliveryTime((short) 60)
          .build();
    }

    public static OrderMenu getMockOrderMenu(Menu menu, Order order, int amount) {
      return OrderMenu.builder()
          .amount(amount)
          .order(order)
          .menu(menu)
          .build();
    }

    public static OrderGroup getMockOrderGroup(Group group, OrderMenu orderMenu) {
      return OrderGroup.builder()
          .group(group)
          .orderMenu(orderMenu)
          .build();
    }

    public static OrderOption getMockOrderOption(Option option, OrderGroup orderGroup) {
      return OrderOption.builder()
          .option(option)
          .orderGroup(orderGroup)
          .build();
    }
  }
}
