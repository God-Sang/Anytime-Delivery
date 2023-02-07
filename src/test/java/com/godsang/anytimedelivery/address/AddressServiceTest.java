package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AddressServiceTest {
  @Autowired
  private AddressService addressService;

  @Test
  void searchTest() {
    // given and when
    AddressDto.SearchResponseDto response = addressService.searchAddress("정자동 178-4", 1, 10);

    // then
    assertThat(response.getResults().getJuso().get(0).getRoadAddr())
        .isEqualTo("경기도 성남시 분당구 정자일로 95(정자동)");
  }
}
