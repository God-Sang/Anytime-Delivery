package com.godsang.anytimedelivery.store.repository;

import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
