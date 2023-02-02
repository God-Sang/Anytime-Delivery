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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class storeRepositoryTest {
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  private final String storeNamePrefix = "피자";
  private final Long categoryId1 = 1L;
  private final Long categoryId2 = 2L;
  private final int numberOfStoresCreated = 10;
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
          .close_time(LocalTime.now())
          .open_time(LocalTime.now())
          .delivery_fee(0)
          .name(storeNamePrefix + i)
          .tel("1234")
          .registrationNumber("1234")
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
    Category categoryAfter = categoryRepository.findById(1L).get();
    String categoryName = categoryAfter.getCategoryStores().get(0).getCategory().getName();
    //then
    assertThat(categoryName)
        .isIn(categoryName);
  }

  @Test
  @DisplayName("카테고리 ID를 통한 스토어 탐색")
  void findStoresByCategoryIdTest() {
    Long categoryId = categoryId1;
    int size = 3;
    List<Store> stores = storeRepository.findStoresByCategory(categoryId, PageRequest.of(0, size));

    assertThat(stores.size())
        .isEqualTo(size);
  }
}
