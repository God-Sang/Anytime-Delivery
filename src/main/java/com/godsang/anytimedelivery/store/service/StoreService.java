package com.godsang.anytimedelivery.store.service;

import com.godsang.anytimedelivery.category.service.CategoryService;
import com.godsang.anytimedelivery.store.entity.Store;
import com.godsang.anytimedelivery.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service logics for Stores
 */
@Service
@RequiredArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final CategoryService categoryService;

  /**
   * search stores by category id and page information.`
   * @param categoryId
   * @param pageable
   * @return
   */
  public Page<Store> findByCategoryId(Long categoryId, Pageable pageable) {
    categoryService.checkCategoryId(categoryId);
    return storeRepository.findStoresByCategory(categoryId, pageable);
  }
}
