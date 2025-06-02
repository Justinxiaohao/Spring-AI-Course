package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.dto.RadioProgramDTO;
import cn.sdtbu.edu.dto.UserProfileDTO;
import cn.sdtbu.edu.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据邮箱查找用户
     */
    User findByEmail(String email);

    /**
     * 根据ID获取用户个人资料（包含统计信息）
     * @param userId 用户ID
     * @return 用户个人资料
     */
    UserProfileDTO selectUserProfile(@Param("userId") Integer userId);

    /**
     * 获取用户喜欢的节目列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 节目列表
     */
    IPage<RadioProgramDTO> selectUserLikedPrograms(Page<RadioProgramDTO> page, @Param("userId") Integer userId);

    /**
     * 检查用户名是否已存在（排除当前用户）
     * @param username 用户名
     * @param excludeUserId 排除的用户ID
     * @return 记录数量
     */
    int checkUsernameExists(@Param("username") String username, @Param("excludeUserId") Integer excludeUserId);

    /**
     * 检查邮箱是否已存在（排除当前用户）
     * @param email 邮箱
     * @param excludeUserId 排除的用户ID
     * @return 记录数量
     */
    int checkEmailExists(@Param("email") String email, @Param("excludeUserId") Integer excludeUserId);
}