package cn.sdtbu.edu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 电台节目信息实体类
 * @author Wyh
 */
@Getter
@Setter
@TableName("radio_programs")
public class RadioProgram {
    
    /**
     * 节目ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 节目名称，不能为空
     */
    @TableField(value = "title")
    private String title;

    /**
     * 节目简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 音频文件访问URL，不能为空
     */
    @TableField(value = "audio_url")
    private String audioUrl;

    /**
     * 封面图片URL
     */
    @TableField(value = "cover_image_url")
    private String coverImageUrl;

    /**
     * 分类ID，外键关联categories表
     */
    @TableField(value = "category_id")
    private Integer categoryId;

    /**
     * 艺术家/播讲人/主播名
     */
    @TableField(value = "artist_narrator")
    private String artistNarrator;

    /**
     * 所属专辑/系列（可选）
     */
    @TableField(value = "album")
    private String album;

    /**
     * 节目时长（秒）
     */
    @TableField(value = "duration_seconds")
    private Integer durationSeconds;

    /**
     * 标签，逗号分隔（如：冥想,放松,正念）
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 发布日期（如果节目有原始发布日期）
     */
    @TableField(value = "publication_date")
    private Date publicationDate;

    /**
     * 播放次数（应用内统计），默认为0
     */
    @TableField(value = "plays_count")
    private Integer playsCount;

    /**
     * 喜欢次数（应用内统计），默认为0
     */
    @TableField(value = "likes_count")
    private Integer likesCount;

    /**
     * 评论数量（应用内统计），默认为0
     */
    @TableField(value = "comments_count")
    private Integer commentsCount;

    /**
     * 是否精选/推荐：1是，0否，默认为0
     */
    @TableField(value = "is_featured")
    private Boolean isFeatured;

    /**
     * 节目状态：published-已发布，draft-草稿，archived-已归档，默认为published
     */
    @TableField(value = "status")
    private String status;

    /**
     * 数据入库时间，插入时自动填充
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
    public RadioProgram() {
    }

    /**
     * 基本构造函数
     * @param title 节目名称
     * @param audioUrl 音频文件URL
     */
    public RadioProgram(String title, String audioUrl) {
        this.title = title;
        this.audioUrl = audioUrl;
        this.playsCount = 0;
        this.likesCount = 0;
        this.commentsCount = 0;
        this.isFeatured = false;
        this.status = "published";
    }

    /**
     * 完整构造函数
     * @param title 节目名称
     * @param description 节目简介
     * @param audioUrl 音频文件URL
     * @param coverImageUrl 封面图片URL
     * @param categoryId 分类ID
     * @param artistNarrator 艺术家/播讲人/主播名
     */
    public RadioProgram(String title, String description, String audioUrl, 
                       String coverImageUrl, Integer categoryId, String artistNarrator) {
        this.title = title;
        this.description = description;
        this.audioUrl = audioUrl;
        this.coverImageUrl = coverImageUrl;
        this.categoryId = categoryId;
        this.artistNarrator = artistNarrator;
        this.playsCount = 0;
        this.likesCount = 0;
        this.commentsCount = 0;
        this.isFeatured = false;
        this.status = "published";
    }
}
