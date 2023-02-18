package com.godsang.anytimedelivery.menu.repository;

import com.godsang.anytimedelivery.menu.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
  /**
   * N + 1 문제를 해결하기 위해 fetch join 사용
   */
  @Query("select distinct g from OPTION_GROUP g join fetch g.options where g.menu.menuId = ?1")
  List<Group> findAllByMenuId(long menuId);
}
