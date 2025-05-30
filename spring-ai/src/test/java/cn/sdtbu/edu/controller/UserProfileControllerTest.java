package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.ApiResponse;
import cn.sdtbu.edu.dto.UserProfileDTO;
import cn.sdtbu.edu.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserProfileController 基于邮箱的API测试类
 * @author Wyh
 */
@WebMvcTest(UserProfileController.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserProfileDTO testProfile;
    private String testEmail;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        
        testProfile = new UserProfileDTO();
        testProfile.setId(1);
        testProfile.setEmail(testEmail);
        testProfile.setUsername("testuser");
        testProfile.setAvatar("/img/avatar01.png");
        testProfile.setBio("测试用户简介");
        testProfile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        testProfile.setLikedProgramsCount(5);
        testProfile.setPlaylistsCount(2);
        testProfile.setCommentsCount(10);
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        when(userService.getUserProfileByEmail(testEmail)).thenReturn(testProfile);

        mockMvc.perform(get("/api/me")
                .header("User-Email", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("获取用户信息成功"))
                .andExpect(jsonPath("$.data.id").value(testProfile.getId()))
                .andExpect(jsonPath("$.data.email").value(testProfile.getEmail()))
                .andExpect(jsonPath("$.data.username").value(testProfile.getUsername()))
                .andExpect(jsonPath("$.data.likedProgramsCount").value(testProfile.getLikedProgramsCount()));

        verify(userService).getUserProfileByEmail(testEmail);
    }

    @Test
    void testGetUserProfile_NoEmail() throws Exception {
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户未登录或邮箱地址无效"));

        verify(userService, never()).getUserProfileByEmail(anyString());
    }

    @Test
    void testGetUserProfile_EmptyEmail() throws Exception {
        mockMvc.perform(get("/api/me")
                .header("User-Email", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("用户未登录或邮箱地址无效"));

        verify(userService, never()).getUserProfileByEmail(anyString());
    }

    @Test
    void testGetUserProfile_EmailNotFound() throws Exception {
        when(userService.getUserProfileByEmail(testEmail))
                .thenThrow(new RuntimeException("邮箱地址不存在"));

        mockMvc.perform(get("/api/me")
                .header("User-Email", testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("邮箱地址不存在"));

        verify(userService).getUserProfileByEmail(testEmail);
    }

    @Test
    void testGetUserLikedPrograms_Success() throws Exception {
        mockMvc.perform(get("/api/me/liked-programs")
                .header("User-Email", testEmail)
                .param("page", "1")
                .param("limit", "10"))
                .andExpect(status().isOk());

        verify(userService).getUserLikedProgramsByEmail(testEmail, 1, 10);
    }

    @Test
    void testGetUserPlaylists_Success() throws Exception {
        mockMvc.perform(get("/api/me/playlists")
                .header("User-Email", testEmail))
                .andExpect(status().isOk());

        verify(userService).getUserPlaylistsByEmail(testEmail);
    }

    @Test
    void testGetUserComments_Success() throws Exception {
        mockMvc.perform(get("/api/me/comments")
                .header("User-Email", testEmail)
                .param("page", "1")
                .param("limit", "10"))
                .andExpect(status().isOk());

        verify(userService).getUserCommentsByEmail(testEmail, 1, 10);
    }
}
