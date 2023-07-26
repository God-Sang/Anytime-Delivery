package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.category.repository.CategoryRepository;
import com.godsang.anytimedelivery.deliveryArea.repository.DeliveryAreaRepository;
import com.godsang.anytimedelivery.helper.cache.CacheUtils;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceCacheTest {
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private DeliveryAreaRepository deliveryAreaRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  private RedisCacheManager cacheManager;
  private StoreSaveUtil storeSaveUtil;
  private List<Store> stores;

  @BeforeAll
  void saveStores() {
    storeSaveUtil = new StoreSaveUtil(storeRepository, categoryRepository,
        deliveryAreaRepository, userRepository);
    int numberOfStores = 30;
    int numberOfDeliveryAreas = 3;
    int numberOfCategoriesAStoreHas = 2;
    int numberOfDeliveryAreasAStoreHas = 2;

    stores = storeSaveUtil.saveStores(numberOfStores, numberOfDeliveryAreas,
        numberOfCategoriesAStoreHas, numberOfDeliveryAreasAStoreHas);
  }

  @BeforeEach
  void beforeClear() {
    CacheUtils.clearCache(redisTemplate);
    cacheManager.initializeCaches();
  }

  @Test
  @Order(100)
  void cacheableTest() {
    // given
    long categoryId = 1L;
    long deliveryAreaId = 1L;
    int size = 10;
    int page = 0;

    // when
    Page<Store> stores =
        storeRepository.findStoresByCategoryAndDeliveryArea(categoryId, deliveryAreaId, PageRequest.of(page, size));

    // then
    Set<String> cacheNames = (Set<String>) cacheManager.getCacheNames();
    assertThat(cacheNames.size()).isSameAs(1);
    if (cacheNames.size() != 1) return;
    Cache cache = null;
    for (String cacheName : cacheNames) cache = cacheManager.getCache(cacheName);
    String key = "page" + page + "size" + size;
    Page<Store> cachedStore = (Page<Store>) cache.get(key).get();
    assertThat(cachedStore.getTotalElements()).isEqualTo(stores.getTotalElements());
    assertThat(cachedStore.getNumber()).isEqualTo(stores.getNumber());
    assertThat(cachedStore.getSize()).isEqualTo(stores.getSize());
    assertThat(cachedStore.getContent().get(0).getAddress()).isEqualTo(stores.getContent().get(0).getAddress());
  }

  @Test
  @Order(200)
  void cacheableTest_EmptyResult() {
    // given
    long categoryId = 99999L;
    long deliveryAreaId = 99999L;
    int size = 10;
    int page = 0;

    // when
    Page<Store> stores =
        storeRepository.findStoresByCategoryAndDeliveryArea(categoryId, deliveryAreaId, PageRequest.of(page, size));

    // then
    Set<String> cacheNames = (Set<String>) cacheManager.getCacheNames();
    assertThat(cacheNames.size()).isSameAs(1);
    if (cacheNames.size() != 1) return;
    Cache cache = null;
    for (String cacheName : cacheNames) cache = cacheManager.getCache(cacheName);
    String key = "page" + page + "size" + size;
    Page<Store> cachedStore = (Page<Store>) cache.get(key).get();
    assertThat(cachedStore.getTotalElements()).isEqualTo(stores.getTotalElements());
    assertThat(cachedStore.getNumber()).isEqualTo(stores.getNumber());
    assertThat(cachedStore.getSize()).isEqualTo(stores.getSize());
    assertThat(cachedStore.getContent().size()).isEqualTo(0);
  }

  @Test
  @Order(300)
  void cacheEvictTest() {
    Collection<String> names = cacheManager.getCacheNames();
    // given -> make 3 Caches : storeCa1De1::page0size10, storeCa2De2::page0size10, storeCa3De3::page0size10
    for (int id = 1; id <= 3; id++) {
      long categoryId = id;
      long deliveryAreaId = id;
      int size = 10;
      int page = 0;

      storeRepository.findStoresByCategoryAndDeliveryArea(categoryId, deliveryAreaId, PageRequest.of(page, size));
    }

    List<Long> categoryIds = List.of(1L, 2L);
    List<Long> deliveryAreaIds = List.of(2L, 3L);

    // when -> evict 1 Cache : storeCa2De2::page0size10
    Store store = storeSaveUtil.createStore(categoryIds, deliveryAreaIds);
    storeRepository.save(store);

    // then
    for (String cacheName : cacheManager.getCacheNames()) {
      assertThat(cacheName).isNotEqualTo("storeCa2De2::page0size10");
    }
  }
}
