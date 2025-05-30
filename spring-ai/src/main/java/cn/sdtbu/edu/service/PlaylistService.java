package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.*;
import cn.sdtbu.edu.entity.Playlist;
import cn.sdtbu.edu.entity.PlaylistItem;
import cn.sdtbu.edu.mapper.PlaylistMapper;
import cn.sdtbu.edu.mapper.PlaylistItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 歌单服务类
 * @author Wyh
 */
@Service
public class PlaylistService extends ServiceImpl<PlaylistMapper, Playlist> {

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistItemMapper playlistItemMapper;

    /**
     * 创建歌单
     * @param userId 用户ID
     * @param request 创建请求
     * @return 创建的歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public PlaylistDTO createPlaylist(Integer userId, CreatePlaylistRequest request) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (request == null || !StringUtils.hasText(request.getName())) {
            throw new IllegalArgumentException("歌单名称不能为空");
        }
        if (request.getName().length() > 100) {
            throw new IllegalArgumentException("歌单名称不能超过100个字符");
        }

        // 检查是否已有同名歌单
        int existsCount = playlistMapper.checkPlaylistNameExists(userId, request.getName(), null);
        if (existsCount > 0) {
            throw new RuntimeException("您已有同名歌单，请使用其他名称");
        }

        // 创建歌单
        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);

        int result = playlistMapper.insert(playlist);
        if (result <= 0) {
            throw new RuntimeException("创建歌单失败");
        }

        // 返回创建的歌单详情
        return playlistMapper.selectPlaylistDetailById(playlist.getId());
    }

    /**
     * 获取用户的歌单列表
     * @param userId 用户ID
     * @return 歌单列表
     */
    public List<PlaylistDTO> getUserPlaylists(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

        return playlistMapper.selectUserPlaylists(userId);
    }

    /**
     * 获取歌单详情
     * @param playlistId 歌单ID
     * @param userId 用户ID（用于权限检查）
     * @return 歌单详情
     */
    public PlaylistDTO getPlaylistDetail(Integer playlistId, Integer userId) {
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }

        PlaylistDTO playlist = playlistMapper.selectPlaylistDetailById(playlistId);
        if (playlist == null) {
            throw new RuntimeException("歌单不存在");
        }

        // 检查权限：只有歌单所有者或公开歌单才能查看
        if (userId == null || (!playlist.getUserId().equals(userId) && !playlist.getIsPublic())) {
            throw new RuntimeException("无权限查看此歌单");
        }

        // 获取歌单内的节目列表
        List<PlaylistItemDTO> items = playlistItemMapper.selectPlaylistItems(playlistId);
        playlist.setItems(items);

        return playlist;
    }

    /**
     * 更新歌单
     * @param playlistId 歌单ID
     * @param userId 用户ID
     * @param request 更新请求
     * @return 更新后的歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public PlaylistDTO updatePlaylist(Integer playlistId, Integer userId, UpdatePlaylistRequest request) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (request == null) {
            throw new IllegalArgumentException("更新请求不能为空");
        }

        // 检查歌单所有权
        int ownershipCount = playlistMapper.checkPlaylistOwnership(playlistId, userId);
        if (ownershipCount == 0) {
            throw new RuntimeException("歌单不存在或无权限修改");
        }

        // 检查歌单名称是否重复（如果要更新名称）
        if (StringUtils.hasText(request.getName())) {
            if (request.getName().length() > 100) {
                throw new IllegalArgumentException("歌单名称不能超过100个字符");
            }
            int existsCount = playlistMapper.checkPlaylistNameExists(userId, request.getName(), playlistId);
            if (existsCount > 0) {
                throw new RuntimeException("您已有同名歌单，请使用其他名称");
            }
        }

        // 更新歌单
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        if (StringUtils.hasText(request.getName())) {
            playlist.setName(request.getName());
        }
        if (request.getDescription() != null) {
            playlist.setDescription(request.getDescription());
        }
        if (request.getIsPublic() != null) {
            playlist.setIsPublic(request.getIsPublic());
        }

        int result = playlistMapper.updateById(playlist);
        if (result <= 0) {
            throw new RuntimeException("更新歌单失败");
        }

        // 返回更新后的歌单详情
        return playlistMapper.selectPlaylistDetailById(playlistId);
    }

    /**
     * 删除歌单
     * @param playlistId 歌单ID
     * @param userId 用户ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePlaylist(Integer playlistId, Integer userId) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

        // 检查歌单所有权
        int ownershipCount = playlistMapper.checkPlaylistOwnership(playlistId, userId);
        if (ownershipCount == 0) {
            throw new RuntimeException("歌单不存在或无权限删除");
        }

        try {
            // 先删除歌单内的所有项目
            playlistItemMapper.deleteByPlaylistId(playlistId);

            // 再删除歌单
            int result = playlistMapper.deleteById(playlistId);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除歌单失败: " + e.getMessage(), e);
        }
    }
}
