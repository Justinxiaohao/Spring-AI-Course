package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.entity.UserProgramLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
 * 用户喜欢节目Mapper接口
 * 由于MyBatis-Plus不支持复合主键，需要自定义SQL处理联合主键操作
 * @author Wyh
 */
@Mapper
public interface UserProgramLikeMapper extends BaseMapper<UserProgramLike> {

    /**
     * 检查用户是否已经喜欢某个节目
     * @param userId 用户ID
     * @param programId 节目ID
     * @return 记录数量（0表示未喜欢，1表示已喜欢）
     */
    @Select("SELECT COUNT(*) FROM user_program_likes WHERE user_id = #{userId} AND program_id = #{programId}")
    int checkUserLikeExists(@Param("userId") Integer userId, @Param("programId") Integer programId);

    /**
     * 添加用户喜欢记录
     * @param userId 用户ID
     * @param programId 节目ID
     * @return 影响行数
     */
    @Insert("INSERT INTO user_program_likes (user_id, program_id, created_at) VALUES (#{userId}, #{programId}, NOW())")
    int insertUserLike(@Param("userId") Integer userId, @Param("programId") Integer programId);

    /**
     * 删除用户喜欢记录
     * @param userId 用户ID
     * @param programId 节目ID
     * @return 影响行数
     */
    @Delete("DELETE FROM user_program_likes WHERE user_id = #{userId} AND program_id = #{programId}")
    int deleteUserLike(@Param("userId") Integer userId, @Param("programId") Integer programId);

    /**
     * 获取用户喜欢的节目数量
     * @param userId 用户ID
     * @return 喜欢的节目数量
     */
    @Select("SELECT COUNT(*) FROM user_program_likes WHERE user_id = #{userId}")
    int countUserLikes(@Param("userId") Integer userId);
}
