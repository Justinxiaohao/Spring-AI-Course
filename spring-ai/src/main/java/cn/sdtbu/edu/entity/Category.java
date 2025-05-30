package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 电台节目分类实体类
 * @author Wyh
 */
@Getter
@Setter
@TableName("categories")
public class Category {

    /**
     * 分类ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称，唯一，不能为空，最大长度100
     */
    @TableField(value = "name")
    private String name;

    /**
     * 分类描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    /**
     * 无参构造函数
     */
    public Category() {
    }

    /**
     * 带参构造函数
     * @param name 分类名称
     * @param description 分类描述
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}