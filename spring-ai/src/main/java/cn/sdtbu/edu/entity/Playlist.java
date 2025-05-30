package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 用户创建的歌单实体类
 * @author Wyh
 */
@Getter
@Setter
@TableName("playlists")
public class Playlist {

    /**
     * 歌单ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID，外键关联users表
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 歌单名称，不能为空，最大长度100
     */
    @TableField(value = "name")
    private String name;

    /**
     * 歌单描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 是否公开：1是，0否，默认为1
     */
    @TableField(value = "is_public")
    private Boolean isPublic;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    /**
     * 更新时间，插入和更新时自动填充
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Timestamp updatedAt;

    /**
     * 无参构造函数
     */
    public Playlist() {
    }

    /**
     * 带参构造函数
     * @param userId 用户ID
     * @param name 歌单名称
     * @param description 歌单描述
     */
    public Playlist(Integer userId, String name, String description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = true; // 默认公开
    }

    /**
     * 带参构造函数（包含是否公开）
     * @param userId 用户ID
     * @param name 歌单名称
     * @param description 歌单描述
     * @param isPublic 是否公开
     */
    public Playlist(Integer userId, String name, String description, Boolean isPublic) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
    }
}