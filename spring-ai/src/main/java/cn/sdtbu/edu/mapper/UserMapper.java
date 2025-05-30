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
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    /**
     * 根据ID获取用户个人资料（包含统计信息）
     * @param userId 用户ID
     * @return 用户个人资料
     */
    @Select({
        "SELECT ",
        "    u.id, u.username, u.email, u.avatar, u.bio, u.created_at as createdAt, ",
        "    COALESCE(likes_count.count, 0) as likedProgramsCount, ",
        "    COALESCE(playlists_count.count, 0) as playlistsCount, ",
        "    COALESCE(comments_count.count, 0) as commentsCount ",
        "FROM users u ",
        "LEFT JOIN (",
        "    SELECT user_id, COUNT(*) as count ",
        "    FROM user_program_likes ",
        "    GROUP BY user_id",
        ") likes_count ON u.id = likes_count.user_id ",
        "LEFT JOIN (",
        "    SELECT user_id, COUNT(*) as count ",
        "    FROM playlists ",
        "    GROUP BY user_id",
        ") playlists_count ON u.id = playlists_count.user_id ",
        "LEFT JOIN (",
        "    SELECT user_id, COUNT(*) as count ",
        "    FROM comments ",
        "    GROUP BY user_id",
        ") comments_count ON u.id = comments_count.user_id ",
        "WHERE u.id = #{userId}"
    })
    UserProfileDTO selectUserProfile(@Param("userId") Integer userId);

    /**
     * 获取用户喜欢的节目列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 节目列表
     */
    @Select({
        "SELECT ",
        "    rp.id, rp.title, rp.description, rp.audio_url as audioUrl, ",
        "    rp.cover_image_url as coverImageUrl, rp.artist_narrator as artistNarrator, ",
        "    rp.duration_seconds as durationSeconds, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, rp.comments_count as commentsCount, ",
        "    rp.tags, rp.is_featured as isFeatured, rp.status, ",
        "    rp.created_at as createdAt, rp.updated_at as updatedAt, ",
        "    c.name as categoryName, upl.created_at as likedAt ",
        "FROM user_program_likes upl ",
        "JOIN radio_programs rp ON upl.program_id = rp.id ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE upl.user_id = #{userId} AND rp.status = 'published' ",
        "ORDER BY upl.created_at DESC"
    })
    IPage<RadioProgramDTO> selectUserLikedPrograms(Page<RadioProgramDTO> page, @Param("userId") Integer userId);

    /**
     * 检查用户名是否已存在（排除当前用户）
     * @param username 用户名
     * @param excludeUserId 排除的用户ID
     * @return 记录数量
     */
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM users ",
        "WHERE username = #{username} ",
        "<if test='excludeUserId != null'>",
        "    AND id != #{excludeUserId} ",
        "</if>",
        "</script>"
    })
    int checkUsernameExists(@Param("username") String username, @Param("excludeUserId") Integer excludeUserId);

    /**
     * 检查邮箱是否已存在（排除当前用户）
     * @param email 邮箱
     * @param excludeUserId 排除的用户ID
     * @return 记录数量
     */
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM users ",
        "WHERE email = #{email} ",
        "<if test='excludeUserId != null'>",
        "    AND id != #{excludeUserId} ",
        "</if>",
        "</script>"
    })
    int checkEmailExists(@Param("email") String email, @Param("excludeUserId") Integer excludeUserId);
}