package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataRedisTest
@MockBean(JpaMetamodelMappingContext.class)
public class AddressRepositoryTest {
  @Autowired
  private AddressRepository addressRepository;

  @Test
  void saveAndRetrieveTest() {
    // given
    Long userId = 1L;
    Address address = Address.builder()
        .userId(userId)
        .oldAddress("정자동 178-4")
        .newAddress("경기 성남시 분당구 정자일로 95")
        .detailAddress("10층 페이팀")
        .dong("정자동")
        .build();
    // when
    addressRepository.save(address);
    Optional<Address> retrieved = addressRepository.findById(userId);

    // then
    assertThat(retrieved.isPresent())
        .isTrue();
  }
}
