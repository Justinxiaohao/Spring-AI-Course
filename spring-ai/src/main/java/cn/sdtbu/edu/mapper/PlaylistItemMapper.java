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
    List<PlaylistItemDTO> selectPlaylistItems(@Param("playlistId") Integer playlistId);

    /**
     * 检查节目是否已在歌单中
     * @param playlistId 歌单ID
     * @param programId 节目ID
     * @return 记录数量
     */
    int checkProgramInPlaylist(@Param("playlistId") Integer playlistId, @Param("programId") Integer programId);

    /**
     * 获取歌单中的最大显示顺序
     * @param playlistId 歌单ID
     * @return 最大显示顺序
     */
    int getMaxDisplayOrder(@Param("playlistId") Integer playlistId);

    /**
     * 检查歌单项是否属于指定歌单
     * @param itemId 歌单项ID
     * @param playlistId 歌单ID
     * @return 记录数量
     */
    int checkItemInPlaylist(@Param("itemId") Integer itemId, @Param("playlistId") Integer playlistId);

    /**
     * 批量更新歌单项的显示顺序
     * @param itemId 歌单项ID
     * @param displayOrder 新的显示顺序
     * @return 影响行数
     */
    int updateDisplayOrder(@Param("itemId") Integer itemId, @Param("displayOrder") Integer displayOrder);

    /**
     * 根据歌单ID删除所有歌单项
     * @param playlistId 歌单ID
     * @return 影响行数
     */
    int deleteByPlaylistId(@Param("playlistId") Integer playlistId);

    /**
     * 获取歌单项详情
     * @param itemId 歌单项ID
     * @return 歌单项详情
     */
    PlaylistItemDTO selectItemDetailById(@Param("itemId") Integer itemId);
}
