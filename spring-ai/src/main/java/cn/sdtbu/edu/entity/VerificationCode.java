package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
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

}
