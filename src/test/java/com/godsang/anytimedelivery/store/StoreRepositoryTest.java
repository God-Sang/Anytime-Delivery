package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreRepositoryTest {
  private final String storeNamePrefix = "피자";
  private final Long categoryId1 = 1L;
  private final Long categoryId2 = 2L;
  private final int numberOfStoresCreated = 10;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  private List<String> categoryName = new ArrayList<>();

  @BeforeAll
  void init() {
    Category category1 = categoryRepository.findById(categoryId1).get();
    Category category2 = categoryRepository.findById(categoryId2).get();
    categoryName.add(category1.getName());
    categoryName.add(category2.getName());

    List<Store> stores = new ArrayList<>();
    for (int i = 0; i < numberOfStoresCreated; i++) {
      Store store = Store.builder()
          .address("aa" + i)
          .closeTime(LocalTime.now())
          .openTime(LocalTime.now())
          .deliveryFee(0)
          .name(storeNamePrefix + i)
          .tel("1234" + i)
          .registrationNumber("1234" + i)
          .categoryStores(new ArrayList<>())
          .build();
      CategoryStore categoryStore1 = new CategoryStore(category1, store);
      CategoryStore categoryStore2 = new CategoryStore(category2, store);
      store.addCategoryStore(categoryStore1);
      store.addCategoryStore(categoryStore2);
      stores.add(store);
    }
    storeRepository.saveAll(stores);
  }

  @Test
  @DisplayName("연관관계 그래프 탐색")
  void associationTest() {
    //when
    Category category = categoryRepository.findById(1L).get();
    String categoryName = category.getCategoryStores().get(0).getCategory().getName();
    //then
    assertThat(categoryName)
        .isIn(categoryName);
  }

  @Test
  @DisplayName("카테고리 ID를 통한 스토어 탐색")
  void findStoresByCategoryIdTest() {
    Long categoryId = categoryId1;
    int size = 3;
    Page<Store> stores = storeRepository.findStoresByCategory(categoryId, PageRequest.of(0, size));

    assertThat(stores.stream().count())
        .isEqualTo(size);
  }
}
