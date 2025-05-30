package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 电台节目数据传输对象
 * 用于主页节目展示的响应数据
 * @author Wyh
 */
@Getter
@Setter
public class RadioProgramDTO {
    
    /**
     * 节目ID
     */
    private Integer id;

    /**
     * 节目名称
     */
    private String title;

    /**
     * 节目简介
     */
    private String description;

    /**
     * 音频文件访问URL
     */
    private String audioUrl;

    /**
     * 封面图片URL
     */
    private String coverImageUrl;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 艺术家/播讲人/主播名
     */
    private String artistNarrator;

    /**
     * 所属专辑/系列
     */
    private String album;

    /**
     * 节目时长（秒）
     */
    private Integer durationSeconds;

    /**
     * 标签，逗号分隔
     */
    private String tags;

    /**
     * 发布日期
     */
    private Date publicationDate;

    /**
     * 播放次数
     */
    private Integer playsCount;

    /**
     * 喜欢次数
     */
    private Integer likesCount;

    /**
     * 评论数量
     */
    private Integer commentsCount;

    /**
     * 是否精选/推荐
     */
    private Boolean isFeatured;

    /**
     * 节目状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;

    /**
     * 无参构造函数
     */
    public RadioProgramDTO() {
    }

    /**
     * 基本信息构造函数
     * @param id 节目ID
     * @param title 节目名称
     * @param coverImageUrl 封面图片URL
     * @param artistNarrator 艺术家/播讲人/主播名
     * @param likesCount 喜欢次数
     * @param playsCount 播放次数
     */
    public RadioProgramDTO(Integer id, String title, String coverImageUrl, 
                          String artistNarrator, Integer likesCount, Integer playsCount) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.artistNarrator = artistNarrator;
        this.likesCount = likesCount;
        this.playsCount = playsCount;
    }

    /**
     * 带分类名称的构造函数
     * @param id 节目ID
     * @param title 节目名称
     * @param coverImageUrl 封面图片URL
     * @param artistNarrator 艺术家/播讲人/主播名
     * @param likesCount 喜欢次数
     * @param playsCount 播放次数
     * @param categoryName 分类名称
     */
    public RadioProgramDTO(Integer id, String title, String coverImageUrl, 
                          String artistNarrator, Integer likesCount, Integer playsCount, 
                          String categoryName) {
        this.id = id;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.artistNarrator = artistNarrator;
        this.likesCount = likesCount;
        this.playsCount = playsCount;
        this.categoryName = categoryName;
    }
}
