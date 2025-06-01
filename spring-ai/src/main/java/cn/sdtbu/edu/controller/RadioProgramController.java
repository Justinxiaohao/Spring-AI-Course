package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.ApiResponse;
import cn.sdtbu.edu.dto.PageResult;
import cn.sdtbu.edu.dto.RadioProgramDTO;
import cn.sdtbu.edu.service.RadioProgramService;
import cn.sdtbu.edu.service.UserProgramLikeService;
import cn.sdtbu.edu.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 电台节目控制器
 * @author Wyh
 */
@CrossOrigin
@RestController
@RequestMapping("/api/programs")
public class RadioProgramController {

    @Autowired
    private RadioProgramService radioProgramService;

    @Autowired
    private UserProgramLikeService userProgramLikeService;

    /**
     * 主页节目展示 - 分页查询电台节目列表
     * GET /api/programs
     * 
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大100）
     * @param sortBy 排序方式：
     *               - createdAt_desc: 最新
     *               - playsCount_desc: 最热（播放次数）
     *               - likesCount_desc: 最受欢迎（点赞次数）
     *               - isFeatured_desc_createdAt_desc: 精选优先，然后按时间
     *               - commentsCount_desc: 评论最多
     * @param categoryId 分类ID（可选）
     * @param tag 标签（可选）
     * @return 分页结果
     */
    @GetMapping
    public ApiResponse<PageResult<RadioProgramDTO>> getPrograms(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "sortBy", defaultValue = "createdAt_desc") String sortBy,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "tag", required = false) String tag) {
        
        try {
            PageResult<RadioProgramDTO> result = radioProgramService.getPrograms(
                page, limit, sortBy, categoryId, tag
            );
            return ApiResponse.success(result, "获取节目列表成功");
        } catch (Exception e) {
            return ApiResponse.error("获取节目列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取节目详情
     * GET /api/programs/{programId}
     * 
     * @param programId 节目ID
     * @return 节目详情
     */
    @GetMapping("/{programId}")
    public ApiResponse<RadioProgramDTO> getProgramDetail(@PathVariable Integer programId) {
        try {
            RadioProgramDTO program = radioProgramService.getProgramDetail(programId);
            return ApiResponse.success(program, "获取节目详情成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取节目详情失败: " + e.getMessage());
        }
    }

    /**
     * 播放节目（统计播放次数）
     * POST /api/programs/{programId}/play
     * 
     * @param programId 节目ID
     * @return 操作结果
     */
    @PostMapping("/{programId}/play")
    public ApiResponse<Void> playProgram(@PathVariable Integer programId) {
        try {
            boolean success = radioProgramService.incrementPlaysCount(programId);
            if (success) {
                return ApiResponse.success("播放统计成功");
            } else {
                return ApiResponse.error("播放统计失败");
            }
        } catch (Exception e) {
            // 播放统计失败不应影响用户体验，返回成功但记录错误
            System.err.println("播放统计异常: " + e.getMessage());
            return ApiResponse.success("播放开始");
        }
    }

    /**
     * 获取热门节目（按热门度分数排序）
     * GET /api/programs/hot
     *
     * 热门度计算规则：热门度 = 点赞数 * 3.0 + 评论数 * 5.0 + 播放数 * 1.0
     *
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @param withRank 是否包含排名信息（默认false）
     * @return 热门节目列表
     */
    @GetMapping("/hot")
    public ApiResponse<PageResult<RadioProgramDTO>> getHotPrograms(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "withRank", defaultValue = "false") Boolean withRank) {

        try {
            // 参数验证
            if (page != null && page < 1) {
                return ApiResponse.badRequest("页码必须大于0");
            }
            if (limit != null && (limit < 1 || limit > 50)) {
                return ApiResponse.badRequest("每页大小必须在1-50之间");
            }

            PageResult<RadioProgramDTO> result;
            if (withRank != null && withRank) {
                // 获取带排名的热门节目
                result = radioProgramService.getHotProgramsWithRank(page, limit);
            } else {
                // 获取普通热门节目
                result = radioProgramService.getHotPrograms(page, limit);
            }

            return ApiResponse.success(result, "获取热门节目成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取热门节目失败: " + e.getMessage());
        }
    }

    /**
     * 获取精选节目
     * GET /api/programs/featured
     *
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @return 精选节目列表
     */
    @GetMapping("/featured")
    public ApiResponse<PageResult<RadioProgramDTO>> getFeaturedPrograms(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        try {
            PageResult<RadioProgramDTO> result = radioProgramService.getFeaturedPrograms(page, limit);
            return ApiResponse.success(result, "获取精选节目成功");
        } catch (Exception e) {
            return ApiResponse.error("获取精选节目失败: " + e.getMessage());
        }
    }

    /**
     * 喜欢节目
     * POST /api/programs/{programId}/like
     *
     * @param programId 节目ID
     * @param email 用户邮箱（通过请求头传递）
     * @return 操作结果
     */
    @PostMapping("/{programId}/like")
    public ApiResponse<Void> likeProgram(
            @PathVariable Integer programId,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            // 简化的用户认证，实际项目中应该使用JWT等更安全的方式
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            boolean success = userProgramLikeService.likeProgramByEmail(email, programId);
            if (success) {
                return ApiResponse.success("喜欢成功");
            } else {
                return ApiResponse.error("喜欢失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("喜欢节目失败: " + e.getMessage());
        }
    }

    /**
     * 取消喜欢节目
     * DELETE /api/programs/{programId}/like
     *
     * @param programId 节目ID
     * @param email 用户邮箱（通过请求头传递）
     * @return 操作结果
     */
    @DeleteMapping("/{programId}/like")
    public ApiResponse<Void> unlikeProgram(
            @PathVariable Integer programId,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            // 简化的用户认证，实际项目中应该使用JWT等更安全的方式
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.badRequest("用户未登录或邮箱地址无效");
            }

            boolean success = userProgramLikeService.unlikeProgramByEmail(email, programId);
            if (success) {
                return ApiResponse.success("取消喜欢成功");
            } else {
                return ApiResponse.error("取消喜欢失败");
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("取消喜欢节目失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户是否喜欢某个节目
     * GET /api/programs/{programId}/like-status
     *
     * @param programId 节目ID
     * @param email 用户邮箱（通过请求头传递）
     * @return 喜欢状态
     */
    @GetMapping("/{programId}/like-status")
    public ApiResponse<Boolean> checkLikeStatus(
            @PathVariable Integer programId,
            @RequestHeader(value = "User-Email", required = false) String email) {

        try {
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.success(false, "用户未登录");
            }

            boolean isLiked = userProgramLikeService.isUserLikedProgramByEmail(email, programId);
            return ApiResponse.success(isLiked, "获取喜欢状态成功");
        } catch (Exception e) {
            return ApiResponse.error("获取喜欢状态失败: " + e.getMessage());
        }
    }

    /**
     * 搜索节目
     * GET /api/programs/search
     *
     * @param q 搜索关键词
     * @param page 页码（默认1）
     * @param limit 每页大小（默认10，最大50）
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ApiResponse<PageResult<RadioProgramDTO>> searchPrograms(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        try {
            PageResult<RadioProgramDTO> result = radioProgramService.searchPrograms(q, page, limit);

            String message = (q != null && !q.trim().isEmpty())
                ? "搜索节目成功，关键词：" + q
                : "获取所有节目成功";

            return ApiResponse.success(result, message);
        } catch (Exception e) {
            return ApiResponse.error("搜索节目失败: " + e.getMessage());
        }
    }
}
