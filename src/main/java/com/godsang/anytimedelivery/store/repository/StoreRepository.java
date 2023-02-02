package com.godsang.anytimedelivery.store.repository;

import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for stores.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
  @Query("select s from Store s join s.categoryStores cs join cs.category c where c.categoryId=?1")
  List<Store> findStoresByCategory(Long categoryId, Pageable pageable);
}