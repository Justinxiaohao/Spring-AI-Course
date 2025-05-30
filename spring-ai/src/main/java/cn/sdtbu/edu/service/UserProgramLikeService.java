package cn.sdtbu.edu.service;

import cn.sdtbu.edu.entity.UserProgramLike;
import cn.sdtbu.edu.mapper.RadioProgramMapper;
import cn.sdtbu.edu.mapper.UserProgramLikeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户喜欢节目服务类
 * @author Wyh
 */
@Service
public class UserProgramLikeService extends ServiceImpl<UserProgramLikeMapper, UserProgramLike> {

    @Autowired
    private UserProgramLikeMapper userProgramLikeMapper;

    @Autowired
    private RadioProgramMapper radioProgramMapper;

    /**
     * 用户喜欢节目
     * @param userId 用户ID
     * @param programId 节目ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean likeProgram(Integer userId, Integer programId) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }

        // 检查是否已经喜欢过
        int existsCount = userProgramLikeMapper.checkUserLikeExists(userId, programId);
        if (existsCount > 0) {
            throw new RuntimeException("您已经喜欢过这个节目了");
        }

        try {
            // 添加喜欢记录
            int insertResult = userProgramLikeMapper.insertUserLike(userId, programId);
            if (insertResult <= 0) {
                throw new RuntimeException("添加喜欢记录失败");
            }

            // 更新节目的喜欢次数
            int updateResult = radioProgramMapper.incrementLikesCount(programId);
            if (updateResult <= 0) {
                throw new RuntimeException("更新节目喜欢次数失败");
            }

            return true;
        } catch (Exception e) {
            // 事务会自动回滚
            throw new RuntimeException("喜欢节目操作失败: " + e.getMessage(), e);
        }
    }

    /**
     * 用户取消喜欢节目
     * @param userId 用户ID
     * @param programId 节目ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeProgram(Integer userId, Integer programId) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }

        // 检查是否已经喜欢过
        int existsCount = userProgramLikeMapper.checkUserLikeExists(userId, programId);
        if (existsCount == 0) {
            throw new RuntimeException("您还没有喜欢过这个节目");
        }

        try {
            // 删除喜欢记录
            int deleteResult = userProgramLikeMapper.deleteUserLike(userId, programId);
            if (deleteResult <= 0) {
                throw new RuntimeException("删除喜欢记录失败");
            }

            // 更新节目的喜欢次数
            int updateResult = radioProgramMapper.decrementLikesCount(programId);
            if (updateResult <= 0) {
                throw new RuntimeException("更新节目喜欢次数失败");
            }

            return true;
        } catch (Exception e) {
            // 事务会自动回滚
            throw new RuntimeException("取消喜欢节目操作失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查用户是否喜欢某个节目
     * @param userId 用户ID
     * @param programId 节目ID
     * @return 是否喜欢
     */
    public boolean isUserLikedProgram(Integer userId, Integer programId) {
        if (userId == null || userId <= 0 || programId == null || programId <= 0) {
            return false;
        }
        
        try {
            int count = userProgramLikeMapper.checkUserLikeExists(userId, programId);
            return count > 0;
        } catch (Exception e) {
            // 查询失败时返回false，不影响主要功能
            System.err.println("检查用户喜欢状态失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取用户喜欢的节目数量
     * @param userId 用户ID
     * @return 喜欢的节目数量
     */
    public int getUserLikesCount(Integer userId) {
        if (userId == null || userId <= 0) {
            return 0;
        }
        
        try {
            return userProgramLikeMapper.countUserLikes(userId);
        } catch (Exception e) {
            System.err.println("获取用户喜欢数量失败: " + e.getMessage());
            return 0;
        }
    }
}
