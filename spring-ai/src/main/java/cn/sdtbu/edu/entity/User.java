package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 用户实体类
 * @author Wyh
 */
@Getter
@Setter
@TableName("users")
public class User {

    /**
     * 用户ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名，不能为空，唯一
     */
    @TableField(value = "username")
    private String username;

    /**
     * 邮箱，不能为空，唯一
     */
    @TableField(value = "email")
    private String email;

    /**
     * 密码，不能为空
     */
    @TableField(value = "password")
    private String password;

    /**
     * 头像URL
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 个人简介
     */
    @TableField(value = "bio")
    private String bio;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    /**
     * 无参构造函数
     */
    public User() {
    }

    /**
     * 带参构造函数
     * @param username 用户名
     * @param avatar 头像URL
     */
    public User(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    /**
     * 完整构造函数
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @param avatar 头像URL
     * @param bio 个人简介
     */
    public User(String username, String email, String password, String avatar, String bio) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.bio = bio;
    }
}