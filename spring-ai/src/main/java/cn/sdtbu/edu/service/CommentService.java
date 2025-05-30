package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.CommentDTO;
import cn.sdtbu.edu.dto.CreateCommentRequest;
import cn.sdtbu.edu.dto.PageResult;
import cn.sdtbu.edu.entity.Comment;
import cn.sdtbu.edu.mapper.CommentMapper;
import cn.sdtbu.edu.mapper.RadioProgramMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论服务类
 * @author Wyh
 */
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RadioProgramMapper radioProgramMapper;

    /**
     * 发表评论
     * @param programId 节目ID
     * @param userId 用户ID
     * @param request 评论请求
     * @return 创建的评论
     */
    @Transactional(rollbackFor = Exception.class)
    public CommentDTO createComment(Integer programId, Integer userId, CreateCommentRequest request) {
        // 参数校验
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (request == null || !StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        if (request.getContent().length() > 1000) {
            throw new IllegalArgumentException("评论内容不能超过1000个字符");
        }

        // 检查节目是否存在
        try {
            radioProgramMapper.selectById(programId);
        } catch (Exception e) {
            throw new RuntimeException("节目不存在");
        }

        // 如果是回复评论，检查父评论是否存在且属于该节目
        if (request.getParentCommentId() != null) {
            int parentExists = commentMapper.checkCommentExists(request.getParentCommentId());
            if (parentExists == 0) {
                throw new RuntimeException("父评论不存在");
            }
            
            int parentInProgram = commentMapper.checkCommentInProgram(request.getParentCommentId(), programId);
            if (parentInProgram == 0) {
                throw new RuntimeException("父评论不属于该节目");
            }
        }

        try {
            // 创建评论
            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setProgramId(programId);
            comment.setParentCommentId(request.getParentCommentId());
            comment.setContent(request.getContent());

            int result = commentMapper.insert(comment);
            if (result <= 0) {
                throw new RuntimeException("发表评论失败");
            }

            // 更新节目的评论数量
            int updateResult = radioProgramMapper.incrementCommentsCount(programId);
            if (updateResult <= 0) {
                throw new RuntimeException("更新节目评论数量失败");
            }

            // 返回创建的评论详情
            return commentMapper.selectCommentDetailById(comment.getId());
        } catch (Exception e) {
            throw new RuntimeException("发表评论失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取节目的评论列表（分页，只获取顶级评论）
     * @param programId 节目ID
     * @param page 页码
     * @param limit 每页大小
     * @return 评论列表
     */
    public PageResult<CommentDTO> getProgramComments(Integer programId, Integer page, Integer limit) {
        // 参数校验
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10; // 默认每页10条，最大50条
        }

        // 创建分页对象
        Page<CommentDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<CommentDTO> result = commentMapper.selectProgramComments(pageObj, programId);

        // 为每个顶级评论加载回复数量（已在SQL中计算）
        // 如果需要加载部分回复，可以在这里添加逻辑

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 获取评论的回复列表
     * @param commentId 评论ID
     * @return 回复列表
     */
    public List<CommentDTO> getCommentReplies(Integer commentId) {
        // 参数校验
        if (commentId == null || commentId <= 0) {
            throw new IllegalArgumentException("评论ID不能为空或无效");
        }

        // 检查评论是否存在
        int exists = commentMapper.checkCommentExists(commentId);
        if (exists == 0) {
            throw new RuntimeException("评论不存在");
        }

        return commentMapper.selectCommentReplies(commentId);
    }

    /**
     * 获取用户的评论列表
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 评论列表
     */
    public PageResult<CommentDTO> getUserComments(Integer userId, Integer page, Integer limit) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        // 创建分页对象
        Page<CommentDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<CommentDTO> result = commentMapper.selectUserComments(pageObj, userId);

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 获取评论详情
     * @param commentId 评论ID
     * @return 评论详情
     */
    public CommentDTO getCommentDetail(Integer commentId) {
        if (commentId == null || commentId <= 0) {
            throw new IllegalArgumentException("评论ID不能为空或无效");
        }

        CommentDTO comment = commentMapper.selectCommentDetailById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        return comment;
    }

    /**
     * 删除评论（软删除或硬删除，根据业务需求）
     * @param commentId 评论ID
     * @param userId 用户ID（权限检查）
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComment(Integer commentId, Integer userId) {
        // 参数校验
        if (commentId == null || commentId <= 0) {
            throw new IllegalArgumentException("评论ID不能为空或无效");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

        // 获取评论详情，检查权限
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此评论");
        }

        try {
            // 删除评论
            int result = commentMapper.deleteById(commentId);
            if (result > 0) {
                // 更新节目的评论数量
                radioProgramMapper.decrementCommentsCount(comment.getProgramId());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("删除评论失败: " + e.getMessage(), e);
        }
    }
}
