package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.*;
import cn.sdtbu.edu.entity.Playlist;
import cn.sdtbu.edu.entity.PlaylistItem;
import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.mapper.PlaylistMapper;
import cn.sdtbu.edu.mapper.PlaylistItemMapper;
import cn.sdtbu.edu.mapper.UserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Autowired
    private UserMapper userMapper;











    /**
     * 通过邮箱创建歌单
     * @param email 用户邮箱
     * @param request 创建请求
     * @return 创建的歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public PlaylistDTO createPlaylistByEmail(String email, CreatePlaylistRequest request) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (request == null || !StringUtils.hasText(request.getName())) {
            throw new IllegalArgumentException("歌单名称不能为空");
        }
        if (request.getName().length() > 100) {
            throw new IllegalArgumentException("歌单名称不能超过100个字符");
        }

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        Integer userId = user.getId();

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
     * 通过邮箱获取用户的歌单列表
     * @param email 用户邮箱
     * @return 歌单列表
     */
    public List<PlaylistDTO> getUserPlaylistsByEmail(String email) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        return playlistMapper.selectUserPlaylists(user.getId());
    }

    /**
     * 通过邮箱获取歌单详情
     * @param playlistId 歌单ID
     * @param email 用户邮箱（用于权限检查）
     * @return 歌单详情
     */
    public PlaylistDTO getPlaylistDetailByEmail(Integer playlistId, String email) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }

        Integer userId = null;
        if (email != null && !email.trim().isEmpty()) {
            // 根据邮箱查找用户
            User user = userMapper.findByEmail(email);
            if (user != null) {
                userId = user.getId();
            }
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
     * 通过邮箱更新歌单
     * @param playlistId 歌单ID
     * @param email 用户邮箱
     * @param request 更新请求
     * @return 更新后的歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public PlaylistDTO updatePlaylistByEmail(Integer playlistId, String email, UpdatePlaylistRequest request) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (request == null) {
            throw new IllegalArgumentException("更新请求不能为空");
        }

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        Integer userId = user.getId();

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
     * 通过邮箱删除歌单
     * @param playlistId 歌单ID
     * @param email 用户邮箱
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePlaylistByEmail(Integer playlistId, String email) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }

        // 根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        Integer userId = user.getId();

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

    /**
     * 获取所有公开歌单列表（用于主页展示）
     * @return 公开歌单列表
     */
    public List<PlaylistDTO> getPublicPlaylists() {
        try {
            return playlistMapper.selectPublicPlaylists();
        } catch (Exception e) {
            throw new RuntimeException("获取公开歌单列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取所有公开歌单列表（用于主页展示）- 限制数量
     * @param limit 限制数量
     * @return 公开歌单列表
     */
    public List<PlaylistDTO> getPublicPlaylists(Integer limit) {
        try {
            List<PlaylistDTO> playlists = playlistMapper.selectPublicPlaylists();

            // 如果指定了限制数量，则截取前N个
            if (limit != null && limit > 0 && playlists.size() > limit) {
                return playlists.subList(0, limit);
            }

            return playlists;
        } catch (Exception e) {
            throw new RuntimeException("获取公开歌单列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 搜索歌单
     * @param keyword 搜索关键词
     * @param page 页码
     * @param limit 每页大小
     * @return 搜索结果
     */
    public PageResult<PlaylistDTO> searchPlaylists(String keyword, Integer page, Integer limit) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10; // 默认每页10条，最大50条
        }

        // 创建分页对象
        Page<PlaylistDTO> pageObj = new Page<>(page, limit);

        // 执行搜索
        IPage<PlaylistDTO> result = playlistMapper.searchPlaylists(pageObj, keyword);

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }
}
