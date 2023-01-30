package com.godsang.anytimedelivery.auth.utils;

import com.godsang.anytimedelivery.user.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthorityUtils {
    @Value("${mail.address.admin}")
    private String adminMailAddress;
    private final String ADMIN = "ROLE_" + Role.ROLE_ADMIN.getName();
    private final String CUSTOMER = "ROLE_"+ Role.ROLE_CUSTOMER.getName();
    private final String OWNER = "ROLE_" + Role.ROLE_OWNER.getName();
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList(ADMIN, CUSTOMER, OWNER);
    private final List<GrantedAuthority> CUSTOMER_ROLES = AuthorityUtils.createAuthorityList(CUSTOMER);
    private final List<GrantedAuthority> OWNER_ROLES = AuthorityUtils.createAuthorityList(OWNER);

    public Role createRoles(String role, String email) {
        role = role.toUpperCase();

        if (email.equals(adminMailAddress)) {
            return Role.ROLE_ADMIN;
        } else if (role.equals(Role.ROLE_CUSTOMER.getName())) {
            return Role.ROLE_CUSTOMER;
        } else {
            return Role.ROLE_OWNER;
        }
    }
}
