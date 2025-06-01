package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;

/**
 * 歌单数据传输对象
 * @author Wyh
 */
@Getter
@Setter
public class PlaylistDTO {
    
    /**
     * 歌单ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 歌单名称
     */
    private String name;

    /**
     * 歌单描述
     */
    private String description;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;

    /**
     * 歌单内节目数量
     */
    private Integer itemCount;

    /**
     * 歌单内的节目列表（可选，用于详情页面）
     */
    private List<PlaylistItemDTO> items;

    /**
     * 创建者用户名（用于公开歌单展示）
     */
    private String userName;

    /**
     * 创建者头像URL（用于公开歌单展示）
     */
    private String userAvatar;

    /**
     * 无参构造函数
     */
    public PlaylistDTO() {
    }

    /**
     * 基本信息构造函数
     * @param id 歌单ID
     * @param name 歌单名称
     * @param description 歌单描述
     * @param isPublic 是否公开
     * @param itemCount 节目数量
     */
    public PlaylistDTO(Integer id, String name, String description, Boolean isPublic, Integer itemCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.itemCount = itemCount;
    }
}
