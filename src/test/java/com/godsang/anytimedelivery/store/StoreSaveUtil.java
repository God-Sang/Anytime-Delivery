package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.repository.UserRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class StoreSaveUtil {
  private StoreRepository storeRepository;
  private CategoryRepository categoryRepository;
  private DeliveryAreaRepository deliveryAreaRepository;
  private UserRepository userRepository;
  private User owner;
  private List<DeliveryArea> deliveryAreas;
  private List<Category> categories;

  public StoreSaveUtil(StoreRepository storeRepository, CategoryRepository categoryRepository,
                       DeliveryAreaRepository deliveryAreaRepository, UserRepository userRepository) {
    this.storeRepository = storeRepository;
    this.categoryRepository = categoryRepository;
    this.deliveryAreaRepository = deliveryAreaRepository;
    this.userRepository = userRepository;
  }

  public List<Store> saveStores(int numberOfStores, int numberOfDeliveryAreas,
                         int numberOfCategoriesAStoreHas, int numberOfDeliveryAreasAStoreHas) {
    createOwner();
    getCategories();
    createDeliveryAreas(numberOfDeliveryAreas);
    return createStores(numberOfStores, numberOfCategoriesAStoreHas, numberOfDeliveryAreasAStoreHas);
  }

  private void createOwner() {
    User owner = User.builder()
        .userId(1L)
        .email("anytime@email.com")
        .phone("010-1234-5678")
        .nickName("애니타임")
        .password("1q2w3e4r@")
        .role(Role.ROLE_OWNER)
        .build();

    this.owner = userRepository.save(owner);
  }

  private void createCategories(int num) {
    if (num < 0) throw new IllegalArgumentException("numberOfCategories must be equal or greater then 0.");
    categories = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      Category category = categoryRepository.save(new Category(randomStringGenerate()));
      categories.add(category);
    }
  }

  private void createDeliveryAreas(int num) {
    if (num < 0) throw new IllegalArgumentException("numberOfDeliveryAreas must be greater then 0.");
    List<DeliveryArea> deliveryAreaList = new ArrayList<>();
    IntStream.range(0, num).forEach(i -> {
      DeliveryArea deliveryArea = new DeliveryArea("동네" + i);
      deliveryAreaList.add(deliveryArea);
    });
    deliveryAreas = deliveryAreaRepository.saveAll(deliveryAreaList);
  }

  private void getCategories() {
    categories = categoryRepository.findAll();
  }

  // 2019년 기준 배달의 민족 업체 수 30만여 곳 -> 1만으로 가정
  private List<Store> createStores(int num, int cIncluded, int dIncluded) {
    if (num < 0) throw new IllegalArgumentException("numberOfStores must be greater then 0.");
    List<Store> stores = new ArrayList<>();
    int categoryIndex = 0;
    int deliveryIndex = 0;
    for (int i = 0; i < num; i++) {
      stores.add(createStore(categoryIndex, deliveryIndex, cIncluded, dIncluded));
      categoryIndex++;
      deliveryIndex++;
      if (categoryIndex >= categories.size() - cIncluded + 1) categoryIndex = 0;
      if (deliveryIndex >= deliveryAreas.size() - dIncluded + 1) deliveryIndex = 0;
    }
    return storeRepository.saveAll(stores);
  }

  public Store createStore(List<Long> categoryIds, List<Long> deliveryAreaIds) {
    Store store = Store.builder()
        .registrationNumber(generateRandomRegistrationNumber())
        .name(randomStringGenerate())
        .tel("02-123-1234")
        .address("경기도 성남시 분당구 123")
        .openTime(LocalTime.of(9, 30))
        .closeTime(LocalTime.of(21, 30))
        .deliveryFee(1000)
        .deliveryTime(30)
        .build();
    store.setUser(owner);
    List<CategoryStore> categoryStores = new ArrayList<>();
    for (Long id : categoryIds) {
      Optional<Category> optionalCategory = categoryRepository.findById(id);
      Category category = optionalCategory.orElseThrow(() -> new IllegalArgumentException("Category not found"));
      categoryStores.add(new CategoryStore(category, store));
    }
    store.setCategoryStores(categoryStores);

    List<DeliveryAreaStore> deliveryAreaStores = new ArrayList<>();
    for (Long id : deliveryAreaIds) {
      Optional<DeliveryArea> optionalDeliveryArea = deliveryAreaRepository.findById(id);
      DeliveryArea deliveryArea = optionalDeliveryArea.orElseThrow(() -> new IllegalArgumentException("DeliveryArea not found"));
      deliveryAreaStores.add(new DeliveryAreaStore(store, deliveryArea));
    }
    store.setDeliveryAreaStores(deliveryAreaStores);
    return store;
  }
  private Store createStore(int c, int d, int cIncluded, int dIncluded) {
    if (cIncluded > categories.size() || dIncluded > deliveryAreas.size()) {
      throw new IllegalArgumentException("A number of entities A Store includes must be equal or greater then the number of them created");
    }
    Store store = Store.builder()
        .registrationNumber(generateRandomRegistrationNumber())
        .name(randomStringGenerate())
        .tel("02-123-1234")
        .address("경기도 성남시 분당구 123")
        .openTime(LocalTime.of(9, 30))
        .closeTime(LocalTime.of(21, 30))
        .deliveryFee(1000)
        .deliveryTime(30)
        .build();
    store.setUser(owner);
    List<CategoryStore> categoryStores = new ArrayList<>();
    for (int i = c; i < c + cIncluded; i++) {
      categoryStores.add(new CategoryStore(categories.get(i), store));
    }
    store.setCategoryStores(categoryStores);

    List<DeliveryAreaStore> deliveryAreaStores = new ArrayList<>();
    for (int i = d; i < d + dIncluded; i++) {
      deliveryAreaStores.add(new DeliveryAreaStore(store, deliveryAreas.get(i)));
    }
    store.setDeliveryAreaStores(deliveryAreaStores);
    return store;
  }

  private String randomStringGenerate() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      sb.append('a' + ((int) Math.floor(Math.random() * 26)));
    }
    sb.append((int) Math.floor(Math.random() * 10000));
    return sb.toString();
  }

  private String generateRandomRegistrationNumber() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 12; i++) {
      sb.append((int) Math.floor(Math.random() * 10));
    }
    return sb.toString();
  }
}
