package com.godsang.anytimedelivery.user;

import com.godsang.anytimedelivery.helper.StubData;
import com.godsang.anytimedelivery.user.controller.UserController;
import com.godsang.anytimedelivery.user.dto.UserDto;
import com.godsang.anytimedelivery.user.entity.Role;
import com.godsang.anytimedelivery.user.mapper.UserMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccessTest() throws Exception {
        // given
        String email = "delivery@email.com";
        String phone = "010-1234-5678";
        String nickname = "애니타임";
        String role = Role.ROLE_OWNER.getName();
        UserDto.Post postDto = StubData.MockUserDto.getMockPost(email, "1q2w3e4r", phone, nickname, role);
        String content = gson.toJson(postDto);

        given(userMapper.userToResponse(Mockito.any())).willReturn(StubData.MockUserDto.getMockResponse(email, phone, nickname, Role.ROLE_OWNER));

        // when, then
        mockMvc.perform(
                        post("/users/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.phone").value(phone))
                .andExpect(jsonPath("$.data.nickName").value(nickname))
                .andExpect(jsonPath("$.data.role").value(String.valueOf(Role.ROLE_OWNER)));
    }
}
