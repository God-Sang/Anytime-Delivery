package com.godsang.anytimedelivery.address.controller;

import com.godsang.anytimedelivery.address.dto.AddressDto;
import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.address.mapper.AddressMapper;
import com.godsang.anytimedelivery.address.service.AddressService;
import com.godsang.anytimedelivery.auth.utils.UserInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/address")
public class AddressController {
  private final AddressService addressService;
  private final UserInfoUtils userInfoUtils;
  private final AddressMapper addressMapper;

  @PostMapping
  public ResponseEntity registerAddress(@RequestBody @Valid AddressDto.PostRequest postRequest) {
    Long userId = userInfoUtils.extractUserId();
    Address address = addressMapper.postRequestDtoToAddress(postRequest);
    addressService.saveAddress(userId, address);
    return new ResponseEntity(HttpStatus.OK);
  }
}
