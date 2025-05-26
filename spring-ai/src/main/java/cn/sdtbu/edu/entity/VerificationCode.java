package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;

@TableName("verification_codes")
public class VerificationCode {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 使用 email 字段来存储用户邮箱
    @TableField(value = "email")
    private String email;

    private String code;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    @TableField(value = "expires_at")
    private Timestamp expiresAt;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}
