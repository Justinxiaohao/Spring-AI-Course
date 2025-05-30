package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 更新歌单顺序请求DTO
 * @author Wyh
 */
@Getter
@Setter
public class UpdatePlaylistOrderRequest {
    
    /**
     * 歌单项ID列表，按新的顺序排列
     */
    private List<Integer> itemIds;

    /**
     * 无参构造函数
     */
    public UpdatePlaylistOrderRequest() {
    }

    /**
     * 带参构造函数
     * @param itemIds 歌单项ID列表
     */
    public UpdatePlaylistOrderRequest(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }
}
