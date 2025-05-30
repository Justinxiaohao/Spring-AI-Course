package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建评论请求DTO
 * @author Wyh
 */
@Getter
@Setter
public class CreateCommentRequest {
    
    /**
     * 评论内容，必填，最大长度1000
     */
    private String content;

    /**
     * 父评论ID，可选，用于回复评论
     */
    private Integer parentCommentId;

    /**
     * 无参构造函数
     */
    public CreateCommentRequest() {
    }

    /**
     * 带参构造函数
     * @param content 评论内容
     */
    public CreateCommentRequest(String content) {
        this.content = content;
    }

    /**
     * 带参构造函数（用于回复）
     * @param content 评论内容
     * @param parentCommentId 父评论ID
     */
    public CreateCommentRequest(String content, Integer parentCommentId) {
        this.content = content;
        this.parentCommentId = parentCommentId;
    }
}
