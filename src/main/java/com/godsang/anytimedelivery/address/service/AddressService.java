package com.godsang.anytimedelivery.address.service;

import com.godsang.anytimedelivery.address.entity.Address;
import com.godsang.anytimedelivery.common.Exception.BusinessLogicException;
import com.godsang.anytimedelivery.common.Exception.ExceptionCode;
import com.godsang.anytimedelivery.user.entity.User;
import com.godsang.anytimedelivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Save or update or retrieve an address.
 */
@Service
@RequiredArgsConstructor
public class AddressService {
  private final UserService userService;

  /**
   * save an address.
   * if there is an address already saved, then it is changed to the new one.
   * And, the old one is removed automatically.
   * @param userId
   * @param address
   */
  @Transactional
  public void saveAddress(Long userId, Address address) {
    User user = userService.findUser(userId);
    user.setAddress(address);
  }

  /**
   * get an address of a user.
   * @param userId
   * @return Address
   */
  @Transactional
  public Address getAddress(Long userId) {
    User user = userService.findUser(userId);
    Address address = user.getAddress();
    if (address == null) {
      throw new BusinessLogicException(ExceptionCode.ADDRESS_NOT_EXIST);
    }
    return address;
  }
  //TODO: 연관관계 설정 후 제작 예정
  public Long getMyDeliveryArea(Long userId) {
    return null;
  }
}
