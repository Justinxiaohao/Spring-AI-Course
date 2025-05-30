package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.dto.PlaylistItemDTO;
import cn.sdtbu.edu.entity.PlaylistItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 歌单项Mapper接口
 * @author Wyh
 */
@Mapper
public interface PlaylistItemMapper extends BaseMapper<PlaylistItem> {

    /**
     * 获取歌单内的节目列表（带节目信息）
     * @param playlistId 歌单ID
     * @return 歌单项列表
     */
    @Select({
        "SELECT ",
        "    pi.id, pi.playlist_id as playlistId, pi.program_id as programId, ",
        "    pi.display_order as displayOrder, pi.added_at as addedAt, ",
        "    rp.title as programTitle, rp.cover_image_url as programCoverImageUrl, ",
        "    rp.artist_narrator as programArtistNarrator, rp.duration_seconds as programDurationSeconds ",
        "FROM playlist_items pi ",
        "JOIN radio_programs rp ON pi.program_id = rp.id ",
        "WHERE pi.playlist_id = #{playlistId} AND rp.status = 'published' ",
        "ORDER BY pi.display_order ASC, pi.added_at ASC"
    })
    List<PlaylistItemDTO> selectPlaylistItems(@Param("playlistId") Integer playlistId);

    /**
     * 检查节目是否已在歌单中
     * @param playlistId 歌单ID
     * @param programId 节目ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM playlist_items WHERE playlist_id = #{playlistId} AND program_id = #{programId}")
    int checkProgramInPlaylist(@Param("playlistId") Integer playlistId, @Param("programId") Integer programId);

    /**
     * 获取歌单中的最大显示顺序
     * @param playlistId 歌单ID
     * @return 最大显示顺序
     */
    @Select("SELECT COALESCE(MAX(display_order), 0) FROM playlist_items WHERE playlist_id = #{playlistId}")
    int getMaxDisplayOrder(@Param("playlistId") Integer playlistId);

    /**
     * 检查歌单项是否属于指定歌单
     * @param itemId 歌单项ID
     * @param playlistId 歌单ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM playlist_items WHERE id = #{itemId} AND playlist_id = #{playlistId}")
    int checkItemInPlaylist(@Param("itemId") Integer itemId, @Param("playlistId") Integer playlistId);

    /**
     * 批量更新歌单项的显示顺序
     * @param itemId 歌单项ID
     * @param displayOrder 新的显示顺序
     * @return 影响行数
     */
    @Update("UPDATE playlist_items SET display_order = #{displayOrder} WHERE id = #{itemId}")
    int updateDisplayOrder(@Param("itemId") Integer itemId, @Param("displayOrder") Integer displayOrder);

    /**
     * 根据歌单ID删除所有歌单项
     * @param playlistId 歌单ID
     * @return 影响行数
     */
    @Delete("DELETE FROM playlist_items WHERE playlist_id = #{playlistId}")
    int deleteByPlaylistId(@Param("playlistId") Integer playlistId);

    /**
     * 获取歌单项详情
     * @param itemId 歌单项ID
     * @return 歌单项详情
     */
    @Select({
        "SELECT ",
        "    pi.id, pi.playlist_id as playlistId, pi.program_id as programId, ",
        "    pi.display_order as displayOrder, pi.added_at as addedAt, ",
        "    rp.title as programTitle, rp.cover_image_url as programCoverImageUrl, ",
        "    rp.artist_narrator as programArtistNarrator, rp.duration_seconds as programDurationSeconds ",
        "FROM playlist_items pi ",
        "JOIN radio_programs rp ON pi.program_id = rp.id ",
        "WHERE pi.id = #{itemId}"
    })
    PlaylistItemDTO selectItemDetailById(@Param("itemId") Integer itemId);
}
