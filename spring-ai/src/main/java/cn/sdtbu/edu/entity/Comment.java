package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 用户对节目的评论实体类
 * @author Wyh
 */
@Getter
@Setter
@TableName("comments")
public class Comment {

    /**
     * 评论ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID，外键关联users表
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 节目ID，外键关联radio_programs表
     */
    @TableField(value = "program_id")
    private Integer programId;

    /**
     * 父评论ID，用于回复，可为空
     */
    @TableField(value = "parent_comment_id")
    private Integer parentCommentId;

    /**
     * 评论内容，不能为空
     */
    @TableField(value = "content")
    private String content;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    /**
     * 更新时间，插入和更新时自动填充
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;

    /**
     * 无参构造函数
     */
    public Comment() {
    }

    /**
     * 带参构造函数
     * @param userId 用户ID
     * @param programId 节目ID
     * @param content 评论内容
     */
    public Comment(Integer userId, Integer programId, String content) {
        this.userId = userId;
        this.programId = programId;
        this.content = content;
    }

    /**
     * 带父评论ID的构造函数（用于回复）
     * @param userId 用户ID
     * @param programId 节目ID
     * @param parentCommentId 父评论ID
     * @param content 评论内容
     */
    public Comment(Integer userId, Integer programId, Integer parentCommentId, String content) {
        this.userId = userId;
        this.programId = programId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }
}