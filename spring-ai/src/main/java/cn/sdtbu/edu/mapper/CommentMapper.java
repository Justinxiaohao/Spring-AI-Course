package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.dto.CommentDTO;
import cn.sdtbu.edu.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 评论Mapper接口
 * @author Wyh
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 分页获取节目的评论列表（只获取顶级评论，不包含回复）
     * @param page 分页对象
     * @param programId 节目ID
     * @return 评论列表
     */
    IPage<CommentDTO> selectProgramComments(Page<CommentDTO> page, @Param("programId") Integer programId);

    /**
     * 获取评论的回复列表
     * @param parentCommentId 父评论ID
     * @return 回复列表
     */
    List<CommentDTO> selectCommentReplies(@Param("parentCommentId") Integer parentCommentId);

    /**
     * 根据ID获取评论详情（包含用户信息）
     * @param commentId 评论ID
     * @return 评论详情
     */
    CommentDTO selectCommentDetailById(@Param("commentId") Integer commentId);

    /**
     * 检查评论是否存在
     * @param commentId 评论ID
     * @return 记录数量
     */
    int checkCommentExists(@Param("commentId") Integer commentId);

    /**
     * 检查评论是否属于指定节目
     * @param commentId 评论ID
     * @param programId 节目ID
     * @return 记录数量
     */
    int checkCommentInProgram(@Param("commentId") Integer commentId, @Param("programId") Integer programId);

    /**
     * 获取用户的评论列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 评论列表
     */
    IPage<CommentDTO> selectUserComments(Page<CommentDTO> page, @Param("userId") Integer userId);

    /**
     * 获取节目的评论总数（包含回复）
     * @param programId 节目ID
     * @return 评论总数
     */
    int countProgramComments(@Param("programId") Integer programId);
}
