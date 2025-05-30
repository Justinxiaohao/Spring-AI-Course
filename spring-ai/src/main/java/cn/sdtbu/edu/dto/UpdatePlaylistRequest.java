package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 更新歌单请求DTO
 * @author Wyh
 */
@Getter
@Setter
public class UpdatePlaylistRequest {
    
    /**
     * 歌单名称，可选
     */
    private String name;

    /**
     * 歌单描述，可选
     */
    private String description;

    /**
     * 是否公开，可选
     */
    private Boolean isPublic;

    /**
     * 无参构造函数
     */
    public UpdatePlaylistRequest() {
    }

    /**
     * 带参构造函数
     * @param name 歌单名称
     * @param description 歌单描述
     * @param isPublic 是否公开
     */
    public UpdatePlaylistRequest(String name, String description, Boolean isPublic) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
    }
}
