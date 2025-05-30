package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 更新用户个人资料请求DTO
 * @author Wyh
 */
@Getter
@Setter
public class UpdateUserProfileRequest {
    
    /**
     * 用户名，可选
     */
    private String username;

    /**
     * 头像URL，可选
     */
    private String avatar;

    /**
     * 个人简介，可选
     */
    private String bio;

    /**
     * 无参构造函数
     */
    public UpdateUserProfileRequest() {
    }

    /**
     * 带参构造函数
     * @param username 用户名
     * @param avatar 头像URL
     * @param bio 个人简介
     */
    public UpdateUserProfileRequest(String username, String avatar, String bio) {
        this.username = username;
        this.avatar = avatar;
        this.bio = bio;
    }
}
