package com.godsang.anytimedelivery.auth.utils;

import com.godsang.anytimedelivery.user.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthorityUtils {
    private final String ADMIN = Role.ROLE_ADMIN.toString();
    private final String CUSTOMER = Role.ROLE_CUSTOMER.toString();
    private final String OWNER = Role.ROLE_OWNER.toString();
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList(ADMIN, CUSTOMER, OWNER);
    private final List<GrantedAuthority> CUSTOMER_ROLES = AuthorityUtils.createAuthorityList(CUSTOMER);
    private final List<GrantedAuthority> OWNER_ROLES = AuthorityUtils.createAuthorityList(OWNER);
    @Value("${mail.address.admin}")
    private String adminMailAddress;

    /**
     * 사용자의 가입 정보에 따라 role 생성
     *
     * @param role  관리자, 고객, 사장
     * @param email 이메일
     * @return role
     */
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

    /**
     * 로그인 시 role에 따라 권한 정보 생성
     *
     * @Param role 사용자의 권한
     * @Return role에 따른 권한 목록
     */
    public List<GrantedAuthority> createAuthorities(Role role) {
        if (role.equals(Role.ROLE_ADMIN)) {
            return ADMIN_ROLES;
        } else if (role.equals(Role.ROLE_CUSTOMER)) {
            return CUSTOMER_ROLES;
        } else {
            return OWNER_ROLES;
        }
    }
}
