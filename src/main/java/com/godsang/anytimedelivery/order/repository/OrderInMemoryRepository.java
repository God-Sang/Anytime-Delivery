package com.godsang.anytimedelivery.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 장바구니에 담을 때 Order가 저장되는 저장소
 * 장바구니에 담을 때는 하나의 Menu 별로 담긴다. (ex. 치킨 골라서 장바구니 담기 버튼 클릭)
 * 한 명의 고객은 하나의 장바구니 Order만 가질 수 있다.
 * 즉 고객 id를 통해, Order를 찾아서 Menu를 추가한 후 저장하면 된다.
 */
@Repository
@RequiredArgsConstructor
public class OrderInMemoryRepository {
  @Resource(name = "redisTemplate")
  private ValueOperations<Long, Object> operations;

  public void save(Long userId, Object object) {
    operations.set(userId, object);
  }

  public Object retrieve(Long userId) {
    return operations.get(userId);
  }
}
