package com.godsang.anytimedelivery.address.service;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {
  private final UserService userService;

  @Transactional
  public void saveAddress(Long userId, Address address) {
    User user = userService.findUser(userId);
    user.setAddress(address);
  }
}
