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
    List<PlaylistDTO> selectUserPlaylists(@Param("userId") Integer userId);

    /**
     * 根据ID获取歌单详情（包含节目数量）
     * @param playlistId 歌单ID
     * @return 歌单详情
     */
    PlaylistDTO selectPlaylistDetailById(@Param("playlistId") Integer playlistId);

    /**
     * 检查歌单是否属于指定用户
     * @param playlistId 歌单ID
     * @param userId 用户ID
     * @return 记录数量
     */
    int checkPlaylistOwnership(@Param("playlistId") Integer playlistId, @Param("userId") Integer userId);

    /**
     * 检查用户是否已有同名歌单
     * @param userId 用户ID
     * @param name 歌单名称
     * @param excludeId 排除的歌单ID（用于更新时检查）
     * @return 记录数量
     */
    int checkPlaylistNameExists(@Param("userId") Integer userId, @Param("name") String name,
                               @Param("excludeId") Integer excludeId);

    /**
     * 获取所有公开歌单列表（用于主页展示）
     * @return 公开歌单列表
     */
    List<PlaylistDTO> selectPublicPlaylists();

    /**
     * 搜索歌单
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 歌单列表
     */
    IPage<PlaylistDTO> searchPlaylists(Page<PlaylistDTO> page, @Param("keyword") String keyword);
}
