package cn.sdtbu.edu.service;

import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.entity.UserProgramLike;
import cn.sdtbu.edu.mapper.RadioProgramMapper;
import cn.sdtbu.edu.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;









    /**
     * 用户通过邮箱喜欢节目
     * @param email 用户邮箱
     * @param programId 节目ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean likeProgramByEmail(String email, Integer programId) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        Integer userId = user.getId();

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
     * 用户通过邮箱取消喜欢节目
     * @param email 用户邮箱
     * @param programId 节目ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeProgramByEmail(String email, Integer programId) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        Integer userId = user.getId();

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
     * 通过邮箱检查用户是否喜欢某个节目
     * @param email 用户邮箱
     * @param programId 节目ID
     * @return 是否喜欢
     */
    public boolean isUserLikedProgramByEmail(String email, Integer programId) {
        if (email == null || email.trim().isEmpty() || programId == null || programId <= 0) {
            return false;
        }

        try {
            // 根据邮箱查找用户
            User user = userMapper.findByEmail(email);
            if (user == null) {
                return false;
            }

            // 检查用户是否喜欢该节目
            int count = userProgramLikeMapper.checkUserLikeExists(user.getId(), programId);
            return count > 0;
        } catch (Exception e) {
            // 查询失败时返回false，不影响主要功能
            System.err.println("检查用户喜欢状态失败: " + e.getMessage());
            return false;
        }
    }
}
