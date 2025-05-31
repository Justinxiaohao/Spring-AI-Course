package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.ApiResponse;
import cn.sdtbu.edu.dto.CommentDTO;
import cn.sdtbu.edu.dto.CreateCommentRequest;
import cn.sdtbu.edu.dto.PageResult;
import cn.sdtbu.edu.service.CommentService;
import cn.sdtbu.edu.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 * @author Wyh
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 发表评论或回复评论
     * POST /api/programs/{programId}/comments
     *
     * @param programId 节目ID
     * @param request 评论请求
     * @param email 用户邮箱（通过请求头传递）
     * @return 创建的评论
     */
    @PostMapping("/programs/{programId}/comments")
    public ApiResponse<CommentDTO> createComment(
            @PathVariable Integer programId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            CommentDTO comment = commentService.createCommentByEmail(programId, email, request);

            String message = request.getParentCommentId() != null ? "回复评论成功" : "发表评论成功";
            return ApiResponse.success(comment, message);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * 获取节目的评论列表
     * GET /api/programs/{programId}/comments
     * 
     * @param programId 节目ID
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @return 评论列表
     */
    @GetMapping("/programs/{programId}/comments")
    public ApiResponse<PageResult<CommentDTO>> getProgramComments(
            @PathVariable Integer programId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        
        try {
            PageResult<CommentDTO> result = commentService.getProgramComments(programId, page, limit);
            return ApiResponse.success(result, "获取评论列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取评论列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取评论的回复列表
     * GET /api/comments/{commentId}/replies
     * 
     * @param commentId 评论ID
     * @return 回复列表
     */
    @GetMapping("/comments/{commentId}/replies")
    public ApiResponse<List<CommentDTO>> getCommentReplies(@PathVariable Integer commentId) {
        
        try {
            List<CommentDTO> replies = commentService.getCommentReplies(commentId);
            return ApiResponse.success(replies, "获取回复列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取回复列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的评论列表
     * GET /api/users/{email}/comments
     *
     * @param email 用户邮箱
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @return 评论列表
     */
    @GetMapping("/users/{email}/comments")
    public ApiResponse<PageResult<CommentDTO>> getUserComments(
            @PathVariable String email,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        try {
            PageResult<CommentDTO> result = commentService.getUserCommentsByEmail(email, page, limit);
            return ApiResponse.success(result, "获取用户评论列表成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取用户评论列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取评论详情
     * GET /api/comments/{commentId}
     * 
     * @param commentId 评论ID
     * @return 评论详情
     */
    @GetMapping("/comments/{commentId}")
    public ApiResponse<CommentDTO> getCommentDetail(@PathVariable Integer commentId) {
        
        try {
            CommentDTO comment = commentService.getCommentDetail(commentId);
            return ApiResponse.success(comment, "获取评论详情成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取评论详情失败: " + e.getMessage());
        }
    }

    /**
     * 删除评论
     * DELETE /api/comments/{commentId}
     *
     * @param commentId 评论ID
     * @param email 用户邮箱（通过请求头传递）
     * @return 操作结果
     */
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Integer commentId,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            boolean success = commentService.deleteCommentByEmail(commentId, email);
            if (success) {
                return ApiResponse.success("删除评论成功");
            } else {
                return ApiResponse.error("删除评论失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("删除评论失败: " + e.getMessage());
        }
    }
}
