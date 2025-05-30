package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.*;
import cn.sdtbu.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 个人中心控制器
 * @author Wyh
 */
@CrossOrigin
@RestController
@RequestMapping("/api/me")
public class UserProfileController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * GET /api/me
     *
     * @param email 用户邮箱地址（通过请求头传递）
     * @return 用户个人资料
     */
    @GetMapping
    public ApiResponse<UserProfileDTO> getUserProfile(
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            UserProfileDTO profile = userService.getUserProfileByEmail(email);
            return ApiResponse.success(profile, "获取用户信息成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 编辑用户信息
     * PUT /api/me
     *
     * @param request 更新用户资料请求
     * @param email 用户邮箱地址（通过请求头传递）
     * @return 更新后的用户资料
     */
    @PutMapping
    public ApiResponse<UserProfileDTO> updateUserProfile(
            @RequestBody UpdateUserProfileRequest request,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            UserProfileDTO profile = userService.updateUserProfileByEmail(email, request);
            return ApiResponse.success(profile, "更新用户信息成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("更新用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 编辑用户信息（支持头像上传）
     * POST /api/me/update-with-avatar
     *
     * @param username 用户名（可选）
     * @param bio 个人简介（可选）
     * @param avatar 头像文件（可选）
     * @param email 用户邮箱地址（通过请求头传递）
     * @return 更新后的用户资料
     */
    @PostMapping("/update-with-avatar")
    public ApiResponse<UserProfileDTO> updateUserProfileWithAvatar(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            // 构建更新请求对象
            UpdateUserProfileRequest request = new UpdateUserProfileRequest();
            request.setUsername(username);
            request.setBio(bio);

            UserProfileDTO profile = userService.updateUserProfileWithAvatarByEmail(email, request, avatar);
            return ApiResponse.success(profile, "更新用户信息成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("更新用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户喜欢的节目
     * GET /api/me/liked-programs
     *
     * @param email 用户邮箱地址（通过请求头传递）
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @return 用户喜欢的节目列表
     */
    @GetMapping("/liked-programs")
    public ApiResponse<PageResult<RadioProgramDTO>> getUserLikedPrograms(
            @RequestHeader(value = "User-Email", required = false) String email,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            PageResult<RadioProgramDTO> result = userService.getUserLikedProgramsByEmail(email, page, limit);
            return ApiResponse.success(result, "获取喜欢的节目列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取喜欢的节目列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的歌单
     * GET /api/me/playlists
     *
     * @param email 用户邮箱地址（通过请求头传递）
     * @return 用户的歌单列表
     */
    @GetMapping("/playlists")
    public ApiResponse<List<PlaylistDTO>> getUserPlaylists(
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            List<PlaylistDTO> playlists = userService.getUserPlaylistsByEmail(email);
            return ApiResponse.success(playlists, "获取用户歌单列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取用户歌单列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的评论
     * GET /api/me/comments
     *
     * @param email 用户邮箱地址（通过请求头传递）
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @return 用户的评论列表
     */
    @GetMapping("/comments")
    public ApiResponse<PageResult<CommentDTO>> getUserComments(
            @RequestHeader(value = "User-Email", required = false) String email,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            PageResult<CommentDTO> result = userService.getUserCommentsByEmail(email, page, limit);
            return ApiResponse.success(result, "获取用户评论列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取用户评论列表失败: " + e.getMessage());
        }
    }
}
