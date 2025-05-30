package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.AddProgramToPlaylistRequest;
import cn.sdtbu.edu.dto.PlaylistItemDTO;
import cn.sdtbu.edu.dto.UpdatePlaylistOrderRequest;
import cn.sdtbu.edu.entity.PlaylistItem;
import cn.sdtbu.edu.mapper.PlaylistItemMapper;
import cn.sdtbu.edu.mapper.PlaylistMapper;
import cn.sdtbu.edu.mapper.RadioProgramMapper;
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

    /**
     * 添加节目到歌单
     * @param playlistId 歌单ID
     * @param userId 用户ID
     * @param request 添加请求
     * @return 添加的歌单项
     */
    @Transactional(rollbackFor = Exception.class)
    public PlaylistItemDTO addProgramToPlaylist(Integer playlistId, Integer userId, AddProgramToPlaylistRequest request) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (request == null || request.getProgramId() == null || request.getProgramId() <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }

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
     * 获取歌单内的节目列表
     * @param playlistId 歌单ID
     * @param userId 用户ID（用于权限检查）
     * @return 歌单项列表
     */
    public List<PlaylistItemDTO> getPlaylistItems(Integer playlistId, Integer userId) {
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }

        // 检查歌单是否存在以及权限
        // 这里简化处理，实际应该检查歌单是否公开或用户是否有权限
        return playlistItemMapper.selectPlaylistItems(playlistId);
    }

    /**
     * 从歌单中移除节目
     * @param playlistId 歌单ID
     * @param itemId 歌单项ID
     * @param userId 用户ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProgramFromPlaylist(Integer playlistId, Integer itemId, Integer userId) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("歌单项ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

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
     * 调整歌单内节目顺序
     * @param playlistId 歌单ID
     * @param userId 用户ID
     * @param request 顺序调整请求
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePlaylistOrder(Integer playlistId, Integer userId, UpdatePlaylistOrderRequest request) {
        // 参数校验
        if (playlistId == null || playlistId <= 0) {
            throw new IllegalArgumentException("歌单ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (request == null || request.getItemIds() == null || request.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("歌单项ID列表不能为空");
        }

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
