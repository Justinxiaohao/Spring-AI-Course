package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.AddProgramToPlaylistRequest;
import cn.sdtbu.edu.dto.PlaylistItemDTO;
import cn.sdtbu.edu.dto.UpdatePlaylistOrderRequest;
import cn.sdtbu.edu.entity.PlaylistItem;
import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.mapper.PlaylistItemMapper;
import cn.sdtbu.edu.mapper.PlaylistMapper;
import cn.sdtbu.edu.mapper.RadioProgramMapper;
import cn.sdtbu.edu.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 歌单项服务类
 * @author Wyh
 */
@Service
public class PlaylistItemService extends ServiceImpl<PlaylistItemMapper, PlaylistItem> {

    @Autowired
    private PlaylistItemMapper playlistItemMapper;

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private RadioProgramMapper radioProgramMapper;

    @Autowired
    private UserMapper userMapper;









    /**
     * 通过邮箱添加节目到歌单
     * @param playlistId 歌单ID
     * @param email 用户邮箱
     * @param request 添加请求
     * @return 添加的歌单项
     */
    @Transactional(rollbackFor = Exception.class)
    public PlaylistItemDTO addProgramToPlaylistByEmail(Integer playlistId, String email, AddProgramToPlaylistRequest request) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (request == null || request.getProgramId() == null || request.getProgramId() <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
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
            throw new RuntimeException("歌单不存在或无权限操作");
        }

        // 检查节目是否存在
        try {
            radioProgramMapper.selectById(request.getProgramId());
        } catch (Exception e) {
            throw new RuntimeException("节目不存在");
        }

        // 检查节目是否已在歌单中
        int existsCount = playlistItemMapper.checkProgramInPlaylist(playlistId, request.getProgramId());
        if (existsCount > 0) {
            throw new RuntimeException("节目已在歌单中");
        }

        // 获取下一个显示顺序
        int nextOrder = playlistItemMapper.getMaxDisplayOrder(playlistId) + 1;

        // 添加歌单项
        PlaylistItem item = new PlaylistItem();
        item.setPlaylistId(playlistId);
        item.setProgramId(request.getProgramId());
        item.setDisplayOrder(nextOrder);

        int result = playlistItemMapper.insert(item);
        if (result <= 0) {
            throw new RuntimeException("添加节目到歌单失败");
        }

        // 返回添加的歌单项详情
        return playlistItemMapper.selectItemDetailById(item.getId());
    }

    /**
     * 通过邮箱获取歌单内的节目列表
     * @param playlistId 歌单ID
     * @param email 用户邮箱（用于权限检查）
     * @return 歌单项列表
     */
    public List<PlaylistItemDTO> getPlaylistItemsByEmail(Integer playlistId, String email) {
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }

        // 检查歌单是否存在以及权限
        // 这里简化处理，实际应该检查歌单是否公开或用户是否有权限
        return playlistItemMapper.selectPlaylistItems(playlistId);
    }

    /**
     * 通过邮箱从歌单中移除节目
     * @param playlistId 歌单ID
     * @param itemId 歌单项ID
     * @param email 用户邮箱
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProgramFromPlaylistByEmail(Integer playlistId, Integer itemId, String email) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("歌单项ID不能为空或无效");
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
            throw new RuntimeException("歌单不存在或无权限操作");
        }

        // 检查歌单项是否属于该歌单
        int itemCount = playlistItemMapper.checkItemInPlaylist(itemId, playlistId);
        if (itemCount == 0) {
            throw new RuntimeException("歌单项不存在或不属于该歌单");
        }

        // 删除歌单项
        int result = playlistItemMapper.deleteById(itemId);
        return result > 0;
    }

    /**
     * 通过邮箱调整歌单内节目顺序
     * @param playlistId 歌单ID
     * @param email 用户邮箱
     * @param request 顺序调整请求
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePlaylistOrderByEmail(Integer playlistId, String email, UpdatePlaylistOrderRequest request) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (request == null || request.getItemIds() == null || request.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("歌单项ID列表不能为空");
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
            throw new RuntimeException("歌单不存在或无权限操作");
        }

        try {
            // 批量更新显示顺序
            List<Integer> itemIds = request.getItemIds();
            for (int i = 0; i < itemIds.size(); i++) {
                Integer itemId = itemIds.get(i);

                // 检查歌单项是否属于该歌单
                int itemCount = playlistItemMapper.checkItemInPlaylist(itemId, playlistId);
                if (itemCount == 0) {
                    throw new RuntimeException("歌单项 " + itemId + " 不存在或不属于该歌单");
                }

                // 更新显示顺序（从1开始）
                int result = playlistItemMapper.updateDisplayOrder(itemId, i + 1);
                if (result <= 0) {
                    throw new RuntimeException("更新歌单项 " + itemId + " 顺序失败");
                }
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("调整歌单顺序失败: " + e.getMessage(), e);
        }
    }
}
