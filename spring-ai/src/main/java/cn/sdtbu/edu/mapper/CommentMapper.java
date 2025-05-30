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
    @Select({
        "SELECT ",
        "    c.id, c.user_id as userId, u.username as userName, u.email as userEmail, ",
        "    c.program_id as programId, rp.title as programTitle, ",
        "    c.parent_comment_id as parentCommentId, ",
        "    c.content, c.created_at as createdAt, c.updated_at as updatedAt, ",
        "    (SELECT COUNT(*) FROM comments WHERE parent_comment_id = c.id) as replyCount ",
        "FROM comments c ",
        "JOIN users u ON c.user_id = u.id ",
        "JOIN radio_programs rp ON c.program_id = rp.id ",
        "WHERE c.program_id = #{programId} AND c.parent_comment_id IS NULL ",
        "ORDER BY c.created_at DESC"
    })
    IPage<CommentDTO> selectProgramComments(Page<CommentDTO> page, @Param("programId") Integer programId);

    /**
     * 获取评论的回复列表
     * @param parentCommentId 父评论ID
     * @return 回复列表
     */
    @Select({
        "SELECT ",
        "    c.id, c.user_id as userId, u.username as userName, u.email as userEmail, ",
        "    c.program_id as programId, rp.title as programTitle, ",
        "    c.parent_comment_id as parentCommentId, ",
        "    pc.user_id as parentUserId, pu.username as parentUserName, ",
        "    c.content, c.created_at as createdAt, c.updated_at as updatedAt ",
        "FROM comments c ",
        "JOIN users u ON c.user_id = u.id ",
        "JOIN radio_programs rp ON c.program_id = rp.id ",
        "LEFT JOIN comments pc ON c.parent_comment_id = pc.id ",
        "LEFT JOIN users pu ON pc.user_id = pu.id ",
        "WHERE c.parent_comment_id = #{parentCommentId} ",
        "ORDER BY c.created_at ASC"
    })
    List<CommentDTO> selectCommentReplies(@Param("parentCommentId") Integer parentCommentId);

    /**
     * 根据ID获取评论详情（包含用户信息）
     * @param commentId 评论ID
     * @return 评论详情
     */
    @Select({
        "SELECT ",
        "    c.id, c.user_id as userId, u.username as userName, u.email as userEmail, ",
        "    c.program_id as programId, rp.title as programTitle, ",
        "    c.parent_comment_id as parentCommentId, ",
        "    c.content, c.created_at as createdAt, c.updated_at as updatedAt ",
        "FROM comments c ",
        "JOIN users u ON c.user_id = u.id ",
        "JOIN radio_programs rp ON c.program_id = rp.id ",
        "WHERE c.id = #{commentId}"
    })
    CommentDTO selectCommentDetailById(@Param("commentId") Integer commentId);

    /**
     * 检查评论是否存在
     * @param commentId 评论ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM comments WHERE id = #{commentId}")
    int checkCommentExists(@Param("commentId") Integer commentId);

    /**
     * 检查评论是否属于指定节目
     * @param commentId 评论ID
     * @param programId 节目ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM comments WHERE id = #{commentId} AND program_id = #{programId}")
    int checkCommentInProgram(@Param("commentId") Integer commentId, @Param("programId") Integer programId);

    /**
     * 获取用户的评论列表
     * @param page 分页对象
     * @param userId 用户ID
     * @return 评论列表
     */
    @Select({
        "SELECT ",
        "    c.id, c.user_id as userId, u.username as userName, ",
        "    c.program_id as programId, rp.title as programTitle, ",
        "    c.parent_comment_id as parentCommentId, ",
        "    c.content, c.created_at as createdAt ",
        "FROM comments c ",
        "JOIN users u ON c.user_id = u.id ",
        "JOIN radio_programs rp ON c.program_id = rp.id ",
        "WHERE c.user_id = #{userId} ",
        "ORDER BY c.created_at DESC"
    })
    IPage<CommentDTO> selectUserComments(Page<CommentDTO> page, @Param("userId") Integer userId);

    /**
     * 获取节目的评论总数（包含回复）
     * @param programId 节目ID
     * @return 评论总数
     */
    @Select("SELECT COUNT(*) FROM comments WHERE program_id = #{programId}")
    int countProgramComments(@Param("programId") Integer programId);
}
