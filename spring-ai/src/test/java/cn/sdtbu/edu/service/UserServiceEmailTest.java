package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.UserProfileDTO;
import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService 根据邮箱获取用户信息功能测试类
 * @author Wyh
 */
@ExtendWith(MockitoExtension.class)
class UserServiceEmailTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private String testEmail;
    private User testUser;
    private UserProfileDTO testProfile;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail(testEmail);
        testUser.setUsername("testuser");
        
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
    void testGetUserProfileByEmail_Success() {
        // 模拟根据邮箱查找用户成功
        when(userMapper.findByEmail(testEmail)).thenReturn(testUser);
        // 模拟获取用户详细资料成功
        when(userMapper.selectUserProfile(testUser.getId())).thenReturn(testProfile);

        UserProfileDTO result = userService.getUserProfileByEmail(testEmail);

        assertNotNull(result);
        assertEquals(testProfile.getId(), result.getId());
        assertEquals(testProfile.getEmail(), result.getEmail());
        assertEquals(testProfile.getUsername(), result.getUsername());
        assertEquals(testProfile.getLikedProgramsCount(), result.getLikedProgramsCount());
        
        verify(userMapper).findByEmail(testEmail);
        verify(userMapper).selectUserProfile(testUser.getId());
    }

    @Test
    void testGetUserProfileByEmail_EmailNotFound() {
        // 模拟邮箱不存在
        when(userMapper.findByEmail(testEmail)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserProfileByEmail(testEmail);
        });

        assertEquals("邮箱地址不存在", exception.getMessage());
        verify(userMapper).findByEmail(testEmail);
        verify(userMapper, never()).selectUserProfile(anyInt());
    }

    @Test
    void testGetUserProfileByEmail_ProfileNotFound() {
        // 模拟用户存在但资料获取失败
        when(userMapper.findByEmail(testEmail)).thenReturn(testUser);
        when(userMapper.selectUserProfile(testUser.getId())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserProfileByEmail(testEmail);
        });

        assertEquals("用户资料获取失败", exception.getMessage());
        verify(userMapper).findByEmail(testEmail);
        verify(userMapper).selectUserProfile(testUser.getId());
    }

    @Test
    void testGetUserProfileByEmail_NullEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserProfileByEmail(null);
        });

        assertEquals("邮箱地址不能为空", exception.getMessage());
        verify(userMapper, never()).findByEmail(anyString());
    }

    @Test
    void testGetUserProfileByEmail_EmptyEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserProfileByEmail("   ");
        });

        assertEquals("邮箱地址不能为空", exception.getMessage());
        verify(userMapper, never()).findByEmail(anyString());
    }

    @Test
    void testGetUserProfileByEmail_BlankEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserProfileByEmail("");
        });

        assertEquals("邮箱地址不能为空", exception.getMessage());
        verify(userMapper, never()).findByEmail(anyString());
    }
}
