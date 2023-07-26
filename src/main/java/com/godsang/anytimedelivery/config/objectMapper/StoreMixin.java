package com.godsang.anytimedelivery.config.objectMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.godsang.anytimedelivery.category.entity.CategoryStore;
import com.godsang.anytimedelivery.deliveryArea.entity.DeliveryAreaStore;
import com.godsang.anytimedelivery.menu.entity.Menu;
import com.godsang.anytimedelivery.user.entity.User;

import java.util.ArrayList;
import java.util.List;

public abstract class StoreMixin {
  @JsonIgnore
  private List<CategoryStore> categoryStores = new ArrayList<>();
  @JsonIgnore
  private List<DeliveryAreaStore> deliveryAreaStores = new ArrayList<>();
  @JsonIgnore
  private User user;
  @JsonIgnore
  private List<Menu> menus;
}
