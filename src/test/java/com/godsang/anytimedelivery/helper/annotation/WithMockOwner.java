package com.godsang.anytimedelivery.helper.annotation;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Meta Annotation
 * OWNER의 권한을 가진 MockUser
 */
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = "OWNER")
public @interface WithMockOwner {
}
