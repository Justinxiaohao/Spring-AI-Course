package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 用户喜欢的电台节目实体类
 * 注意：由于MyBatis-Plus不支持复合主键，这里不使用@TableId注解
 * 需要在Mapper中自定义SQL来处理联合主键的操作
 * @author Wyh
 */
@Getter
@Setter
@TableName("user_program_likes")
public class UserProgramLike {

    /**
     * 用户ID，联合主键的一部分
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 节目ID，联合主键的一部分
     */
    @TableField(value = "program_id")
    private Integer programId;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    /**
     * 无参构造函数
     */
    public UserProgramLike() {
    }

    /**
     * 带参构造函数
     * @param userId 用户ID
     * @param programId 节目ID
     */
    public UserProgramLike(Integer userId, Integer programId) {
        this.userId = userId;
        this.programId = programId;
    }

    /**
     * 重写equals方法，用于比较两个UserProgramLike对象是否相等
     * @param obj 比较对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserProgramLike that = (UserProgramLike) obj;
        return userId.equals(that.userId) && programId.equals(that.programId);
    }

    /**
     * 重写hashCode方法
     * @return hash值
     */
    @Override
    public int hashCode() {
        return userId.hashCode() + programId.hashCode();
    }

    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "UserProgramLike{" +
                "userId=" + userId +
                ", programId=" + programId +
                ", createdAt=" + createdAt +
                '}';
    }
}
