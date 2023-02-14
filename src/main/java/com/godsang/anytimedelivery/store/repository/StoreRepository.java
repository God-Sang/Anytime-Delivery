package com.godsang.anytimedelivery.store.repository;

import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for stores.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
  @Query("select s " +
            "from Store s " +
            "join s.deliveryAreaStores das " +
            "join das.deliveryArea da " +
            "join s.categoryStores cs " +
            "join cs.category c " +
            "where c.categoryId = ?1 " +
              "and da.deliveryAreaId = ?2"
            )
  Page<Store> findStoresByCategoryAndDeliveryArea(Long categoryId, Long deliveryAreaId, Pageable pageable);

  boolean existsByRegistrationNumber(String registrationNumber);

  boolean existsByTel(String tel);

  boolean existsByAddress(String address);

  boolean existsByName(String name);
}
