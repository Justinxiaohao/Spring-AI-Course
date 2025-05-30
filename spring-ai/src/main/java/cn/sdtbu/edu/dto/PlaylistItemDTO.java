package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * 歌单项数据传输对象
 * @author Wyh
 */
@Getter
@Setter
public class PlaylistItemDTO {
    
    /**
     * 歌单项ID
     */
    private Integer id;

    /**
     * 歌单ID
     */
    private Integer playlistId;

    /**
     * 节目ID
     */
    private Integer programId;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 添加时间
     */
    private Timestamp addedAt;

    /**
     * 节目标题
     */
    private String programTitle;

    /**
     * 节目封面图片URL
     */
    private String programCoverImageUrl;

    /**
     * 艺术家/播讲人/主播名
     */
    private String programArtistNarrator;

    /**
     * 节目时长（秒）
     */
    private Integer programDurationSeconds;

    /**
     * 无参构造函数
     */
    public PlaylistItemDTO() {
    }

    /**
     * 完整构造函数
     * @param id 歌单项ID
     * @param playlistId 歌单ID
     * @param programId 节目ID
     * @param displayOrder 显示顺序
     * @param addedAt 添加时间
     * @param programTitle 节目标题
     * @param programCoverImageUrl 节目封面图片URL
     * @param programArtistNarrator 艺术家/播讲人/主播名
     * @param programDurationSeconds 节目时长
     */
    public PlaylistItemDTO(Integer id, Integer playlistId, Integer programId, Integer displayOrder, 
                          Timestamp addedAt, String programTitle, String programCoverImageUrl, 
                          String programArtistNarrator, Integer programDurationSeconds) {
        this.id = id;
        this.playlistId = playlistId;
        this.programId = programId;
        this.displayOrder = displayOrder;
        this.addedAt = addedAt;
        this.programTitle = programTitle;
        this.programCoverImageUrl = programCoverImageUrl;
        this.programArtistNarrator = programArtistNarrator;
        this.programDurationSeconds = programDurationSeconds;
    }
}
