package com.godsang.anytimedelivery.store;

import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.common.page.CustomPage;
import com.godsang.anytimedelivery.helper.cache.CacheUtils;
import com.godsang.anytimedelivery.helper.stub.StubData;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import com.godsang.anytimedelivery.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
public class ServiceCacheTest {
  @MockBean
  private CategoryService categoryService;
  @MockBean
  private StoreRepository storeRepository;
  @Autowired
  private StoreService storeService;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Test
  void cacheableTest() {
    // given
    long categoryId = 1L;
    int page = 1;
    int size = 10;
    long deliveryAreaId = 10L;
    Pageable pageable = PageRequest.of(page, size);
    Page<Store> storePage = makeStorePage();
    given(categoryService.findVerifiedCategoryById(categoryId)).willReturn(null);
    given(storeRepository.findStoresByCategoryAndDeliveryArea(any(), any(), any())).willReturn(storePage);

    // when
    IntStream.range(0, 10)
        .forEach(i -> storeService.findStoresByCategoryId(categoryId, deliveryAreaId, page, size));
    Page<Store> stores = storeService.findStoresByCategoryId(categoryId, deliveryAreaId, page, size);
    // then
    then(storeRepository).should().findStoresByCategoryAndDeliveryArea(categoryId, deliveryAreaId, pageable);

    CacheUtils.clearCache(redisTemplate);
  }

  private Page<Store> makeStorePage() {
    List<Store> stores = new ArrayList<>();
    IntStream.range(1, 5)
        .forEach(i -> stores.add(StubData.MockStore.getMockEntity(i)));
    return new CustomPage<>(stores);
  }
}
