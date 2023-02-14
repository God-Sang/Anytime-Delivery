package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.category.entity.Category;
import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryArea;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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
  @Autowired
  private DeliveryAreaRepository deliveryAreaRepository;
  private List<String> categoryNames = new ArrayList<>();
  private List<Long> deliveryAreaIds = new ArrayList<>();


  @BeforeEach
  void init() {
    Category category1 = categoryRepository.findById(categoryId1).get();
    Category category2 = categoryRepository.findById(categoryId2).get();
    categoryNames.add(category1.getName());
    categoryNames.add(category2.getName());

    DeliveryArea deliveryArea1 = deliveryAreaRepository.save(new DeliveryArea("서울시 강남구 역삼동"));
    DeliveryArea deliveryArea2 = deliveryAreaRepository.save(new DeliveryArea("서울시 강남구 삼성동"));
    deliveryAreaIds.add(deliveryArea1.getDeliveryAreaId());
    deliveryAreaIds.add(deliveryArea2.getDeliveryAreaId());

    List<Store> stores = new ArrayList<>();
    for (int i = 0; i < numberOfStoresCreated; i++) {
      Store store = StubData.MockStore.getMockEntity(null, "1234" + i, storeNamePrefix + i, "1234" + i, "aa" + i);

      CategoryStore categoryStore1 = new CategoryStore(category1, store);
      CategoryStore categoryStore2 = new CategoryStore(category2, store);
      store.addCategoryStore(categoryStore1);
      store.addCategoryStore(categoryStore2);

      store.addDeliveryAreaStore(new DeliveryAreaStore(store, deliveryArea1));
      store.addDeliveryAreaStore(new DeliveryAreaStore(store, deliveryArea2));

      stores.add(store);
    }
    List<Store> savedStores = storeRepository.saveAll(stores);
  }

  @Test
  @DisplayName("연관관계 그래프 탐색1")
  void association1Test() {
    //when
    Category category = categoryRepository.findById(1L).get();
    String categoryName = category.getCategoryStores().get(0).getCategory().getName();
    //then
    assertThat(categoryName)
        .isIn(categoryNames);
  }

  @Test
  @DisplayName("연관관계 그래프 탐색2")
  @Transactional
  void association2Test() {
    //when
    Store store = storeRepository.findById(1L).get();
    Long deliveryAreaId = store.getDeliveryAreaStores().get(0).getDeliveryArea().getDeliveryAreaId();
    //then
    assertThat(deliveryAreaId)
        .isIn(deliveryAreaIds);
  }

  @Test
  @DisplayName("카테고리 ID와 DeliveryAreaId 통한 스토어 탐색")
  void findStoresByCategoryIdTest() {
    //given
    Long deliveryAreaId = deliveryAreaIds.get(0);
    Long categoryId = categoryId1;

    int size = 3;
    Page<Store> stores = storeRepository.findStoresByCategoryAndDeliveryArea(categoryId, deliveryAreaId, PageRequest.of(0, size));

    assertThat(stores.stream().count())
        .isEqualTo(size);
  }
}
