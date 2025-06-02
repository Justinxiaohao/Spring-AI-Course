package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;

/**
 * 评论数据传输对象
 * @author Wyh
 */
@Getter
@Setter
public class CommentDTO {
    
    /**
     * 评论ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户邮箱（可选，用于显示头像等）
     */
    private String userEmail;

    /**
     * 用户头像URL
     */
    private String userAvatar;

    /**
     * 节目ID
     */
    private Integer programId;

    /**
     * 节目标题
     */
    private String programTitle;

    /**
     * 父评论ID
     */
    private Integer parentCommentId;

    /**
     * 父评论用户ID
     */
    private Integer parentUserId;

    /**
     * 父评论用户名（用于回复显示）
     */
    private String parentUserName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;

    /**
     * 回复列表（可选，用于嵌套显示）
     */
    private List<CommentDTO> replies;

    /**
     * 回复数量
     */
    private Integer replyCount;

    /**
     * 无参构造函数
     */
    public CommentDTO() {
    }

    /**
     * 基本信息构造函数
     * @param id 评论ID
     * @param userId 用户ID
     * @param userName 用户名
     * @param programId 节目ID
     * @param content 评论内容
     * @param createdAt 创建时间
     */
    public CommentDTO(Integer id, Integer userId, String userName, Integer programId, 
                     String content, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.programId = programId;
        this.content = content;
        this.createdAt = createdAt;
    }

    /**
     * 完整构造函数
     * @param id 评论ID
     * @param userId 用户ID
     * @param userName 用户名
     * @param userEmail 用户邮箱
     * @param programId 节目ID
     * @param programTitle 节目标题
     * @param parentCommentId 父评论ID
     * @param parentUserName 父评论用户名
     * @param content 评论内容
     * @param createdAt 创建时间
     * @param updatedAt 更新时间
     */
    public CommentDTO(Integer id, Integer userId, String userName, String userEmail,
                     Integer programId, String programTitle, Integer parentCommentId,
                     String parentUserName, String content, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.programId = programId;
        this.programTitle = programTitle;
        this.parentCommentId = parentCommentId;
        this.parentUserName = parentUserName;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
