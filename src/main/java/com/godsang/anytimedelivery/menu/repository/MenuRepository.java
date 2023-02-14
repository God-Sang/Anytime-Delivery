package com.godsang.anytimedelivery.menu.repository;

import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  boolean existsByNameAndStore(String name, Store store);
}
