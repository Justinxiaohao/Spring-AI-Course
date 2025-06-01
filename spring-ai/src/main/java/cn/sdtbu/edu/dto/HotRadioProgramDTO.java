package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 热门电台节目数据传输对象
 * 继承自RadioProgramDTO，增加热门度分数字段
 * @author Wyh
 */
@Getter
@Setter
public class HotRadioProgramDTO extends RadioProgramDTO {
    
    /**
     * 热门度分数
     * 计算公式：热门度 = 点赞数 * 点赞权重 + 评论数 * 评论权重 + 播放数 * 播放权重
     */
    private Double hotScore;
    
    /**
     * 热门度排名
     */
    private Integer hotRank;
    
    /**
     * 无参构造函数
     */
    public HotRadioProgramDTO() {
        super();
    }
    
    /**
     * 从RadioProgramDTO创建HotRadioProgramDTO
     * @param radioProgram 基础节目信息
     * @param hotScore 热门度分数
     */
    public HotRadioProgramDTO(RadioProgramDTO radioProgram, Double hotScore) {
        // 复制父类所有字段
        this.setId(radioProgram.getId());
        this.setTitle(radioProgram.getTitle());
        this.setDescription(radioProgram.getDescription());
        this.setAudioUrl(radioProgram.getAudioUrl());
        this.setCoverImageUrl(radioProgram.getCoverImageUrl());
        this.setCategoryId(radioProgram.getCategoryId());
        this.setCategoryName(radioProgram.getCategoryName());
        this.setArtistNarrator(radioProgram.getArtistNarrator());
        this.setAlbum(radioProgram.getAlbum());
        this.setDurationSeconds(radioProgram.getDurationSeconds());
        this.setTags(radioProgram.getTags());
        this.setPublicationDate(radioProgram.getPublicationDate());
        this.setPlaysCount(radioProgram.getPlaysCount());
        this.setLikesCount(radioProgram.getLikesCount());
        this.setCommentsCount(radioProgram.getCommentsCount());
        this.setIsFeatured(radioProgram.getIsFeatured());
        this.setStatus(radioProgram.getStatus());
        this.setCreatedAt(radioProgram.getCreatedAt());
        this.setUpdatedAt(radioProgram.getUpdatedAt());
        
        // 设置热门度分数
        this.hotScore = hotScore;
    }
    
    /**
     * 计算热门度分数的静态方法
     * @param likesCount 点赞数
     * @param commentsCount 评论数
     * @param playsCount 播放数
     * @return 热门度分数
     */
    public static Double calculateHotScore(Integer likesCount, Integer commentsCount, Integer playsCount) {
        // 定义权重
        final double LIKES_WEIGHT = 3.0;    // 点赞权重
        final double COMMENTS_WEIGHT = 5.0; // 评论权重（评论比点赞更有价值）
        final double PLAYS_WEIGHT = 1.0;    // 播放权重
        
        // 处理null值
        int likes = likesCount != null ? likesCount : 0;
        int comments = commentsCount != null ? commentsCount : 0;
        int plays = playsCount != null ? playsCount : 0;
        
        // 计算热门度分数
        return likes * LIKES_WEIGHT + comments * COMMENTS_WEIGHT + plays * PLAYS_WEIGHT;
    }
    
    /**
     * 获取热门度等级描述
     * @return 热门度等级
     */
    public String getHotLevel() {
        if (hotScore == null) {
            return "未知";
        }
        
        if (hotScore >= 10000) {
            return "超级热门";
        } else if (hotScore >= 5000) {
            return "非常热门";
        } else if (hotScore >= 2000) {
            return "热门";
        } else if (hotScore >= 500) {
            return "较热门";
        } else {
            return "一般";
        }
    }
    
    @Override
    public String toString() {
        return "HotRadioProgramDTO{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", hotScore=" + hotScore +
                ", hotRank=" + hotRank +
                ", likesCount=" + getLikesCount() +
                ", commentsCount=" + getCommentsCount() +
                ", playsCount=" + getPlaysCount() +
                '}';
    }
}
