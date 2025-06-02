package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.dto.RadioProgramDTO;
import cn.sdtbu.edu.entity.RadioProgram;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 电台节目Mapper接口
 * @author Wyh
 */
@Mapper
public interface RadioProgramMapper extends BaseMapper<RadioProgram> {

    /**
     * 分页查询电台节目列表（带分类名称）
     * @param page 分页对象
     * @param categoryId 分类ID（可选）
     * @param tag 标签（可选）
     * @param sortBy 排序字段
     * @return 分页结果
     */
    IPage<RadioProgramDTO> selectProgramsWithCategory(
        Page<RadioProgramDTO> page,
        @Param("categoryId") Integer categoryId,
        @Param("tag") String tag,
        @Param("sortBy") String sortBy
    );

    /**
     * 根据ID查询电台节目详情（带分类名称）
     * @param programId 节目ID
     * @return 节目详情
     */
    RadioProgramDTO selectProgramDetailById(@Param("programId") Integer programId);

    /**
     * 增加播放次数
     * @param programId 节目ID
     * @return 影响行数
     */
    int incrementPlaysCount(@Param("programId") Integer programId);

    /**
     * 增加喜欢次数
     * @param programId 节目ID
     * @return 影响行数
     */
    int incrementLikesCount(@Param("programId") Integer programId);

    /**
     * 减少喜欢次数
     * @param programId 节目ID
     * @return 影响行数
     */
    int decrementLikesCount(@Param("programId") Integer programId);

    /**
     * 增加评论次数
     * @param programId 节目ID
     * @return 影响行数
     */
    int incrementCommentsCount(@Param("programId") Integer programId);

    /**
     * 减少评论次数
     * @param programId 节目ID
     * @return 影响行数
     */
    int decrementCommentsCount(@Param("programId") Integer programId);

    /**
     * 获取热门节目（按播放次数排序）- 旧版本，保留兼容性
     * @param page 分页对象
     * @param limit 限制数量
     * @return 热门节目列表
     */
    IPage<RadioProgramDTO> selectHotProgramsByPlays(
        Page<RadioProgramDTO> page,
        @Param("limit") Integer limit
    );

    /**
     * 获取精选节目
     * @param page 分页对象
     * @return 精选节目列表
     */
    IPage<RadioProgramDTO> selectFeaturedPrograms(Page<RadioProgramDTO> page);

    /**
     * 搜索节目
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 节目列表
     */
    IPage<RadioProgramDTO> searchPrograms(Page<RadioProgramDTO> page, @Param("keyword") String keyword);

    /**
     * 获取热门节目列表（按热门度分数排序）
     * @param page 分页对象
     * @return 热门节目列表
     */
    IPage<RadioProgramDTO> selectHotPrograms(Page<RadioProgramDTO> page);

    /**
     * 获取热门节目列表（带排名）
     * @param page 分页对象
     * @return 热门节目列表
     */
    List<RadioProgramDTO> selectHotProgramsWithRank(Page<RadioProgramDTO> page);
}
