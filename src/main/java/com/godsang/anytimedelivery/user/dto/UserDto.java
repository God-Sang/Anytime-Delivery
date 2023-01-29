package com.godsang.anytimedelivery.user.dto;

import com.godsang.anytimedelivery.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto { // TODO validation
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        private String email;
        private String password;
        private String phone;
        private String nickName;
        private String role;

        @Builder
        public Post(String email, String password, String phone, String nickName, String role) {
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.nickName = nickName;
            this.role = role;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String email;
        private String phone;
        private String nickName;
        private Role role;

        @Builder
        public Response(String email, String phone, String nickName, Role role) {
            this.email = email;
            this.phone = phone;
            this.nickName = nickName;
            this.role = role;
        }
    }
}
