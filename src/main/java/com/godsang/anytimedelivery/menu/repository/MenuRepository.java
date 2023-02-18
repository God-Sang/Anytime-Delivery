package com.godsang.anytimedelivery.menu.repository;

import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
  boolean existsByNameAndStore(String name, Store store);

  @Query("select m from Menu m where m.store.storeId = ?1")
  List<Menu> findMenusByStoreId(long storeId);
}
