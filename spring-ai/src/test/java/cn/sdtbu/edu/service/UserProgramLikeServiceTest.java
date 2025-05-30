package cn.sdtbu.edu.service;

import cn.sdtbu.edu.mapper.RadioProgramMapper;
import cn.sdtbu.edu.mapper.UserProgramLikeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * UserProgramLikeService 测试类
 * @author Wyh
 */
@ExtendWith(MockitoExtension.class)
class UserProgramLikeServiceTest {

    @Mock
    private UserProgramLikeMapper userProgramLikeMapper;

    @Mock
    private RadioProgramMapper radioProgramMapper;

    @InjectMocks
    private UserProgramLikeService userProgramLikeService;

    private Integer testUserId;
    private Integer testProgramId;

    @BeforeEach
    void setUp() {
        testUserId = 1;
        testProgramId = 1;
    }

    @Test
    void testLikeProgram_Success() {
        // 模拟用户还没有喜欢过这个节目
        when(userProgramLikeMapper.checkUserLikeExists(testUserId, testProgramId)).thenReturn(0);
        when(userProgramLikeMapper.insertUserLike(testUserId, testProgramId)).thenReturn(1);
        when(radioProgramMapper.incrementLikesCount(testProgramId)).thenReturn(1);

        boolean result = userProgramLikeService.likeProgram(testUserId, testProgramId);

        assertTrue(result);
        verify(userProgramLikeMapper).checkUserLikeExists(testUserId, testProgramId);
        verify(userProgramLikeMapper).insertUserLike(testUserId, testProgramId);
        verify(radioProgramMapper).incrementLikesCount(testProgramId);
    }

    @Test
    void testLikeProgram_AlreadyLiked() {
        // 模拟用户已经喜欢过这个节目
        when(userProgramLikeMapper.checkUserLikeExists(testUserId, testProgramId)).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userProgramLikeService.likeProgram(testUserId, testProgramId);
        });

        assertEquals("您已经喜欢过这个节目了", exception.getMessage());
        verify(userProgramLikeMapper).checkUserLikeExists(testUserId, testProgramId);
        verify(userProgramLikeMapper, never()).insertUserLike(anyInt(), anyInt());
        verify(radioProgramMapper, never()).incrementLikesCount(anyInt());
    }

    @Test
    void testLikeProgram_InvalidUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userProgramLikeService.likeProgram(null, testProgramId);
        });

        assertEquals("用户ID不能为空或无效", exception.getMessage());
    }

    @Test
    void testLikeProgram_InvalidProgramId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userProgramLikeService.likeProgram(testUserId, null);
        });

        assertEquals("节目ID不能为空或无效", exception.getMessage());
    }

    @Test
    void testUnlikeProgram_Success() {
        // 模拟用户已经喜欢过这个节目
        when(userProgramLikeMapper.checkUserLikeExists(testUserId, testProgramId)).thenReturn(1);
        when(userProgramLikeMapper.deleteUserLike(testUserId, testProgramId)).thenReturn(1);
        when(radioProgramMapper.decrementLikesCount(testProgramId)).thenReturn(1);

        boolean result = userProgramLikeService.unlikeProgram(testUserId, testProgramId);

        assertTrue(result);
        verify(userProgramLikeMapper).checkUserLikeExists(testUserId, testProgramId);
        verify(userProgramLikeMapper).deleteUserLike(testUserId, testProgramId);
        verify(radioProgramMapper).decrementLikesCount(testProgramId);
    }

    @Test
    void testUnlikeProgram_NotLiked() {
        // 模拟用户还没有喜欢过这个节目
        when(userProgramLikeMapper.checkUserLikeExists(testUserId, testProgramId)).thenReturn(0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userProgramLikeService.unlikeProgram(testUserId, testProgramId);
        });

        assertEquals("您还没有喜欢过这个节目", exception.getMessage());
        verify(userProgramLikeMapper).checkUserLikeExists(testUserId, testProgramId);
        verify(userProgramLikeMapper, never()).deleteUserLike(anyInt(), anyInt());
        verify(radioProgramMapper, never()).decrementLikesCount(anyInt());
    }

    @Test
    void testIsUserLikedProgram_True() {
        when(userProgramLikeMapper.checkUserLikeExists(testUserId, testProgramId)).thenReturn(1);

        boolean result = userProgramLikeService.isUserLikedProgram(testUserId, testProgramId);

        assertTrue(result);
        verify(userProgramLikeMapper).checkUserLikeExists(testUserId, testProgramId);
    }

    @Test
    void testIsUserLikedProgram_False() {
        when(userProgramLikeMapper.checkUserLikeExists(testUserId, testProgramId)).thenReturn(0);

        boolean result = userProgramLikeService.isUserLikedProgram(testUserId, testProgramId);

        assertFalse(result);
        verify(userProgramLikeMapper).checkUserLikeExists(testUserId, testProgramId);
    }

    @Test
    void testGetUserLikesCount() {
        int expectedCount = 5;
        when(userProgramLikeMapper.countUserLikes(testUserId)).thenReturn(expectedCount);

        int result = userProgramLikeService.getUserLikesCount(testUserId);

        assertEquals(expectedCount, result);
        verify(userProgramLikeMapper).countUserLikes(testUserId);
    }
}
