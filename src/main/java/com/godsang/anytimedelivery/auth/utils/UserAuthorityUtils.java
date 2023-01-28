package com.godsang.anytimedelivery.auth.utils;

import com.godsang.anytimedelivery.user.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthorityUtils {
    private final List<GrantedAuthority> CUSTOMER_ROLES = AuthorityUtils.createAuthorityList("ROLE_CUSTOMER");
    private final List<GrantedAuthority> OWNER_ROLES = AuthorityUtils.createAuthorityList("ROLE_OWNER");

    public Role createRoles(String role) {
        if (role.equals(Role.ROLE_CUSTOMER.getName())) {
            return Role.ROLE_CUSTOMER;
        }
        return Role.ROLE_OWNER;
    }
}
