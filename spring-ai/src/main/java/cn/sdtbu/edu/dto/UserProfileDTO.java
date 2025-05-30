package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 用户个人资料DTO
 * @author Wyh
 */
@Getter
@Setter
public class UserProfileDTO {
    
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 统计信息
     */
    private Integer likedProgramsCount;
    private Integer playlistsCount;
    private Integer commentsCount;

    /**
     * 无参构造函数
     */
    public UserProfileDTO() {
    }

    /**
     * 基本信息构造函数
     * @param id 用户ID
     * @param username 用户名
     * @param email 邮箱
     * @param avatar 头像URL
     * @param bio 个人简介
     * @param createdAt 创建时间
     */
    public UserProfileDTO(Integer id, String username, String email, String avatar, String bio, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.bio = bio;
        this.createdAt = createdAt;
    }
}
