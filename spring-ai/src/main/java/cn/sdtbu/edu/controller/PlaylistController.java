package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.*;
import cn.sdtbu.edu.service.PlaylistItemService;
import cn.sdtbu.edu.service.PlaylistService;
import cn.sdtbu.edu.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 歌单控制器
 * @author Wyh
 */
@CrossOrigin
@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistItemService playlistItemService;

    /**
     * 创建歌单
     * POST /api/playlists
     * 
     * @param request 创建歌单请求
     * @param userId 用户ID（临时通过请求头传递）
     * @return 创建的歌单
     */
    @PostMapping
    public ApiResponse<PlaylistDTO> createPlaylist(
            @RequestBody CreatePlaylistRequest request,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            UserContext.setCurrentUserId(userId);
            
            PlaylistDTO playlist = playlistService.createPlaylist(userId, request);
            return ApiResponse.success(playlist, "创建歌单成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("创建歌单失败: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 获取当前用户的歌单列表
     * GET /api/playlists
     * 
     * @param userId 用户ID（临时通过请求头传递）
     * @return 歌单列表
     */
    @GetMapping
    public ApiResponse<List<PlaylistDTO>> getUserPlaylists(
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            List<PlaylistDTO> playlists = playlistService.getUserPlaylists(userId);
            return ApiResponse.success(playlists, "获取歌单列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取歌单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取歌单详情
     * GET /api/playlists/{playlistId}
     * 
     * @param playlistId 歌单ID
     * @param userId 用户ID（临时通过请求头传递）
     * @return 歌单详情
     */
    @GetMapping("/{playlistId}")
    public ApiResponse<PlaylistDTO> getPlaylistDetail(
            @PathVariable Integer playlistId,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            PlaylistDTO playlist = playlistService.getPlaylistDetail(playlistId, userId);
            return ApiResponse.success(playlist, "获取歌单详情成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取歌单详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新歌单
     * PUT /api/playlists/{playlistId}
     * 
     * @param playlistId 歌单ID
     * @param request 更新歌单请求
     * @param userId 用户ID（临时通过请求头传递）
     * @return 更新后的歌单
     */
    @PutMapping("/{playlistId}")
    public ApiResponse<PlaylistDTO> updatePlaylist(
            @PathVariable Integer playlistId,
            @RequestBody UpdatePlaylistRequest request,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            UserContext.setCurrentUserId(userId);
            
            PlaylistDTO playlist = playlistService.updatePlaylist(playlistId, userId, request);
            return ApiResponse.success(playlist, "更新歌单成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("更新歌单失败: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 删除歌单
     * DELETE /api/playlists/{playlistId}
     * 
     * @param playlistId 歌单ID
     * @param userId 用户ID（临时通过请求头传递）
     * @return 操作结果
     */
    @DeleteMapping("/{playlistId}")
    public ApiResponse<Void> deletePlaylist(
            @PathVariable Integer playlistId,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            UserContext.setCurrentUserId(userId);
            
            boolean success = playlistService.deletePlaylist(playlistId, userId);
            if (success) {
                return ApiResponse.success("删除歌单成功");
            } else {
                return ApiResponse.error("删除歌单失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("删除歌单失败: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 添加节目到歌单
     * POST /api/playlists/{playlistId}/items
     * 
     * @param playlistId 歌单ID
     * @param request 添加节目请求
     * @param userId 用户ID（临时通过请求头传递）
     * @return 添加的歌单项
     */
    @PostMapping("/{playlistId}/items")
    public ApiResponse<PlaylistItemDTO> addProgramToPlaylist(
            @PathVariable Integer playlistId,
            @RequestBody AddProgramToPlaylistRequest request,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            UserContext.setCurrentUserId(userId);
            
            PlaylistItemDTO item = playlistItemService.addProgramToPlaylist(playlistId, userId, request);
            return ApiResponse.success(item, "添加节目到歌单成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("添加节目到歌单失败: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 获取歌单内的节目列表
     * GET /api/playlists/{playlistId}/items
     * 
     * @param playlistId 歌单ID
     * @param userId 用户ID（临时通过请求头传递）
     * @return 歌单项列表
     */
    @GetMapping("/{playlistId}/items")
    public ApiResponse<List<PlaylistItemDTO>> getPlaylistItems(
            @PathVariable Integer playlistId,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            List<PlaylistItemDTO> items = playlistItemService.getPlaylistItems(playlistId, userId);
            return ApiResponse.success(items, "获取歌单内容成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取歌单内容失败: " + e.getMessage());
        }
    }

    /**
     * 从歌单中移除节目
     * DELETE /api/playlists/{playlistId}/items/{itemId}
     * 
     * @param playlistId 歌单ID
     * @param itemId 歌单项ID
     * @param userId 用户ID（临时通过请求头传递）
     * @return 操作结果
     */
    @DeleteMapping("/{playlistId}/items/{itemId}")
    public ApiResponse<Void> removeProgramFromPlaylist(
            @PathVariable Integer playlistId,
            @PathVariable Integer itemId,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            UserContext.setCurrentUserId(userId);
            
            boolean success = playlistItemService.removeProgramFromPlaylist(playlistId, itemId, userId);
            if (success) {
                return ApiResponse.success("移除节目成功");
            } else {
                return ApiResponse.error("移除节目失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("移除节目失败: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 调整歌单内节目顺序
     * PUT /api/playlists/{playlistId}/items/order
     * 
     * @param playlistId 歌单ID
     * @param request 顺序调整请求
     * @param userId 用户ID（临时通过请求头传递）
     * @return 操作结果
     */
    @PutMapping("/{playlistId}/items/order")
    public ApiResponse<Void> updatePlaylistOrder(
            @PathVariable Integer playlistId,
            @RequestBody UpdatePlaylistOrderRequest request,
            @RequestHeader(value = "User-Id", required = false) Integer userId) {
        
        try {
            if (userId == null || userId <= 0) {
                return ApiResponse.badRequest("用户未登录或用户ID无效");
            }
            
            UserContext.setCurrentUserId(userId);
            
            boolean success = playlistItemService.updatePlaylistOrder(playlistId, userId, request);
            if (success) {
                return ApiResponse.success("调整歌单顺序成功");
            } else {
                return ApiResponse.error("调整歌单顺序失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("调整歌单顺序失败: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }
}
