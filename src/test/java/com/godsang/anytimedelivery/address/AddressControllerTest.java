package com.godsang.anytimedelivery.address;

import com.godsang.anytimedelivery.address.controller.AddressController;
import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.mapper.AddressMapper;
import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.auth.utils.UserInfoUtils;
import com.godsang.anytimedelivery.helper.StubData;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AddressController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
public class AddressControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Gson gson;
  @MockBean
  private AddressService addressService;
  @MockBean
  private AddressMapper addressMapper;
  @MockBean
  private UserInfoUtils userInfoUtils;

  private static Stream<Arguments> provideInvalidPost() {
    return Stream.of(
        Arguments.of("", " ", null),
        Arguments.of("서울시 행복구 행복동", " ", " ")
    );
  }

  private static Stream<Arguments> provideValidPost() {
    return Stream.of(
        Arguments.of("서울시 행복구 행복동", " 105동 102호", "서울시행복구행복동"),
        Arguments.of("서울시 행복구 행복동", null, "서울시행복구행복동")
    );
  }

  @DisplayName("유효한 Post")
  @ParameterizedTest
  @MethodSource("provideValidPost")
  void registerValidTest(String address, String detail, String deliveryArea) throws Exception {
    // given
    AddressDto request = StubData.MockAddress.getMockAddressPostRequestDto(address, detail, deliveryArea);
    String content = gson.toJson(request);

    given(userInfoUtils.extractUserId()).willReturn(1L);

    mockMvc.perform(
            post("/customer/address")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isOk());
  }

  @DisplayName("유효하지 않은 Post")
  @ParameterizedTest
  @MethodSource("provideInvalidPost")
  void registerInValidTest(String address, String detail, String deliveryArea) throws Exception {
    // given
    AddressDto request = StubData.MockAddress.getMockAddressPostRequestDto(address, detail, deliveryArea);
    String content = gson.toJson(request);

    given(userInfoUtils.extractUserId()).willReturn(1L);

    mockMvc.perform(
            post("/customer/address")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
        .andExpect(status().isBadRequest());
  }

}
