package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 歌单包含的节目实体类
 * @author Wyh
 */
@Getter
@Setter
@TableName("playlist_items")
public class PlaylistItem {

    /**
     * 歌单项ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 歌单ID，外键关联playlists表
     */
    @TableField(value = "playlist_id")
    private Integer playlistId;

    /**
     * 节目ID，外键关联radio_programs表
     */
    @TableField(value = "program_id")
    private Integer programId;

    /**
     * 在歌单中的显示顺序，默认为0
     */
    @TableField(value = "display_order")
    private Integer displayOrder;

    /**
     * 添加时间，插入时自动填充
     */
    @TableField(value = "added_at", fill = FieldFill.INSERT)
    private Timestamp addedAt;

    /**
     * 无参构造函数
     */
    public PlaylistItem() {
    }

    /**
     * 带参构造函数
     * @param playlistId 歌单ID
     * @param programId 节目ID
     */
    public PlaylistItem(Integer playlistId, Integer programId) {
        this.playlistId = playlistId;
        this.programId = programId;
        this.displayOrder = 0; // 默认顺序
    }

    /**
     * 带参构造函数（包含显示顺序）
     * @param playlistId 歌单ID
     * @param programId 节目ID
     * @param displayOrder 显示顺序
     */
    public PlaylistItem(Integer playlistId, Integer programId, Integer displayOrder) {
        this.playlistId = playlistId;
        this.programId = programId;
        this.displayOrder = displayOrder;
    }
}