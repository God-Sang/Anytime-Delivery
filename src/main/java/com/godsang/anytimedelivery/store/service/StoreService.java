package com.godsang.anytimedelivery.store.service;

import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final CategoryService categoryService;

  public List<Store> findByCategoryId(Long categoryId, Pageable pageable) {
    categoryService.checkCategoryId(categoryId);
    return storeRepository.findStoresByCategory(categoryId, pageable);
  }
}
