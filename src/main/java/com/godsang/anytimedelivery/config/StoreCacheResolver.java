package com.godsang.anytimedelivery.config;

import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoreCacheResolver extends SimpleCacheResolver {
  public StoreCacheResolver(CacheManager cacheManager) {
    super(cacheManager);
  }

  @Override
  public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
    List<String> cacheNames = makeCacheNames(context);
    Collection<Cache> result = new ArrayList<>(cacheNames.size());
    for (String cacheName : cacheNames) {
      Cache cache = getCacheManager().getCache(cacheName);
      if (cache == null) {
        throw new IllegalArgumentException("Cannot find cache named '" +
            cacheName + "' for " + context.getOperation());
      }
      result.add(cache);
    }
    return result;
  }

  private List<String> makeCacheNames(CacheOperationInvocationContext<?> context) {
    Object[] args = context.getArgs();
    List<String> cacheNames = new ArrayList<>();
    delegate(args, context.getMethod().getDeclaredAnnotations(), cacheNames);
    if (cacheNames.size() == 0)
      throw new IllegalCallerException("No appropriate resolve method found");
    return cacheNames;
  }

  private void delegate(Object[] args, Annotation[] annotations, List<String> cacheNames) {
    for (Annotation annotation : annotations) {
      if (annotation.annotationType() == Cacheable.class) {
        findCacheForCacheable(args, cacheNames);
        break;
      }
      if (annotation.annotationType() == CacheEvict.class) {
        findCacheForEvict(args, cacheNames);
        break;
      }
    }
  }

  private void findCacheForCacheable(Object[] args, List<String> cacheNames) {
    if (args.length < 2) throw new IllegalCallerException("Caller method must have at least 2 parameters.");
    Long categoryId = (Long) args[0];
    Long deliveryAreaId = (Long) args[1];
    String cacheName = makeCacheName(categoryId, deliveryAreaId);
    cacheNames.add(cacheName);
  }

  private void findCacheForEvict(Object[] args, List<String> cacheNames) {
    Store store = (Store) args[0];
    List<Long> categoryIds = new ArrayList<>();
    for (CategoryStore cs : store.getCategoryStores()) {
      categoryIds.add(cs.getCategory().getCategoryId());
    }
    List<Long> deliveryAreaIds = new ArrayList<>();
    for (DeliveryAreaStore das : store.getDeliveryAreaStores()) {
      deliveryAreaIds.add(das.getDeliveryArea().getDeliveryAreaId());
    }
    for (Long categoryId : categoryIds) {
      for (Long deliveryAreaId : deliveryAreaIds) {
        cacheNames.add(makeCacheName(categoryId, deliveryAreaId));
      }
    }
  }

  private String makeCacheName(Long categoryId, Long deliveryAreaId) {
    return "storeCa" + categoryId + "De" + deliveryAreaId;
  }
}
