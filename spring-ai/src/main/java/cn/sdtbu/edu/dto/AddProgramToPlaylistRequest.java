package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 添加节目到歌单请求DTO
 * @author Wyh
 */
@Getter
@Setter
public class AddProgramToPlaylistRequest {
    
    /**
     * 节目ID，必填
     */
    private Integer programId;

    /**
     * 无参构造函数
     */
    public AddProgramToPlaylistRequest() {
    }

    /**
     * 带参构造函数
     * @param programId 节目ID
     */
    public AddProgramToPlaylistRequest(Integer programId) {
        this.programId = programId;
    }
}
