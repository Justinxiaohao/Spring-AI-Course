package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.dto.PlaylistDTO;
import cn.sdtbu.edu.entity.Playlist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 歌单Mapper接口
 * @author Wyh
 */
@Mapper
public interface PlaylistMapper extends BaseMapper<Playlist> {

    /**
     * 获取用户的歌单列表（包含节目数量）
     * @param userId 用户ID
     * @return 歌单列表
     */
    @Select({
        "SELECT ",
        "    p.id, p.user_id as userId, p.name, p.description, p.is_public as isPublic, ",
        "    p.created_at as createdAt, p.updated_at as updatedAt, ",
        "    COALESCE(item_count.count, 0) as itemCount ",
        "FROM playlists p ",
        "LEFT JOIN (",
        "    SELECT playlist_id, COUNT(*) as count ",
        "    FROM playlist_items ",
        "    GROUP BY playlist_id",
        ") item_count ON p.id = item_count.playlist_id ",
        "WHERE p.user_id = #{userId} ",
        "ORDER BY p.created_at DESC"
    })
    List<PlaylistDTO> selectUserPlaylists(@Param("userId") Integer userId);

    /**
     * 根据ID获取歌单详情（包含节目数量）
     * @param playlistId 歌单ID
     * @return 歌单详情
     */
    @Select({
        "SELECT ",
        "    p.id, p.user_id as userId, p.name, p.description, p.is_public as isPublic, ",
        "    p.created_at as createdAt, p.updated_at as updatedAt, ",
        "    COALESCE(item_count.count, 0) as itemCount ",
        "FROM playlists p ",
        "LEFT JOIN (",
        "    SELECT playlist_id, COUNT(*) as count ",
        "    FROM playlist_items ",
        "    GROUP BY playlist_id",
        ") item_count ON p.id = item_count.playlist_id ",
        "WHERE p.id = #{playlistId}"
    })
    PlaylistDTO selectPlaylistDetailById(@Param("playlistId") Integer playlistId);

    /**
     * 检查歌单是否属于指定用户
     * @param playlistId 歌单ID
     * @param userId 用户ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM playlists WHERE id = #{playlistId} AND user_id = #{userId}")
    int checkPlaylistOwnership(@Param("playlistId") Integer playlistId, @Param("userId") Integer userId);

    /**
     * 检查用户是否已有同名歌单
     * @param userId 用户ID
     * @param name 歌单名称
     * @param excludeId 排除的歌单ID（用于更新时检查）
     * @return 记录数量
     */
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM playlists ",
        "WHERE user_id = #{userId} AND name = #{name} ",
        "<if test='excludeId != null'>",
        "    AND id != #{excludeId} ",
        "</if>",
        "</script>"
    })
    int checkPlaylistNameExists(@Param("userId") Integer userId, @Param("name") String name,
                               @Param("excludeId") Integer excludeId);

    /**
     * 获取所有公开歌单列表（用于主页展示）
     * @return 公开歌单列表
     */
    @Select({
        "SELECT ",
        "    p.id, p.user_id as userId, p.name, p.description, p.is_public as isPublic, ",
        "    p.created_at as createdAt, p.updated_at as updatedAt, ",
        "    u.username as userName, u.avatar as userAvatar, ",
        "    COALESCE(item_count.count, 0) as itemCount ",
        "FROM playlists p ",
        "JOIN users u ON p.user_id = u.id ",
        "LEFT JOIN (",
        "    SELECT playlist_id, COUNT(*) as count ",
        "    FROM playlist_items pi ",
        "    JOIN radio_programs rp ON pi.program_id = rp.id ",
        "    WHERE rp.status = 'published' ",
        "    GROUP BY playlist_id",
        ") item_count ON p.id = item_count.playlist_id ",
        "WHERE p.is_public = 1 AND item_count.count > 0 ",
        "ORDER BY p.updated_at DESC, p.created_at DESC"
    })
    List<PlaylistDTO> selectPublicPlaylists();

    /**
     * 搜索歌单
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 歌单列表
     */
    @Select({
        "<script>",
        "SELECT ",
        "    p.id, p.user_id as userId, p.name, p.description, p.is_public as isPublic, ",
        "    p.created_at as createdAt, p.updated_at as updatedAt, ",
        "    u.username as userName, u.avatar as userAvatar, ",
        "    COALESCE(item_count.count, 0) as itemCount ",
        "FROM playlists p ",
        "JOIN users u ON p.user_id = u.id ",
        "LEFT JOIN (",
        "    SELECT playlist_id, COUNT(*) as count ",
        "    FROM playlist_items pi ",
        "    JOIN radio_programs rp ON pi.program_id = rp.id ",
        "    WHERE rp.status = 'published' ",
        "    GROUP BY playlist_id",
        ") item_count ON p.id = item_count.playlist_id ",
        "WHERE p.is_public = 1 AND item_count.count > 0 ",
        "<if test='keyword != null and keyword != \"\"'>",
        "    AND (",
        "        p.name LIKE CONCAT('%', #{keyword}, '%') ",
        "        OR p.description LIKE CONCAT('%', #{keyword}, '%') ",
        "        OR u.username LIKE CONCAT('%', #{keyword}, '%')",
        "    )",
        "</if>",
        "ORDER BY p.updated_at DESC, p.created_at DESC",
        "</script>"
    })
    IPage<PlaylistDTO> searchPlaylists(Page<PlaylistDTO> page, @Param("keyword") String keyword);
}
