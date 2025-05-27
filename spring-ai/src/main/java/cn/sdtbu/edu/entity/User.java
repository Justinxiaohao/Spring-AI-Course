package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Setter
@Getter
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    @TableField(value = "email")
    private String email;
    private String password;
    private String avatar;
    private String bio;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    public User(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public User() {
    }

}