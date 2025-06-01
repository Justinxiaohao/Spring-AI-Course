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
    @Select({
        "<script>",
        "SELECT ",
        "    rp.id, rp.title, rp.description, rp.audio_url as audioUrl, ",
        "    rp.cover_image_url as coverImageUrl, rp.category_id as categoryId, ",
        "    c.name as categoryName, rp.artist_narrator as artistNarrator, ",
        "    rp.album, rp.duration_seconds as durationSeconds, rp.tags, ",
        "    rp.publication_date as publicationDate, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, rp.comments_count as commentsCount, ",
        "    rp.is_featured as isFeatured, rp.status, ",
        "    rp.created_at as createdAt, rp.updated_at as updatedAt ",
        "FROM radio_programs rp ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE rp.status = 'published' ",
        "<if test='categoryId != null'>",
        "    AND rp.category_id = #{categoryId} ",
        "</if>",
        "<if test='tag != null and tag != \"\"'>",
        "    AND FIND_IN_SET(#{tag}, rp.tags) ",
        "</if>",
        "<choose>",
        "    <when test='sortBy == \"createdAt_desc\"'>",
        "        ORDER BY rp.created_at DESC",
        "    </when>",
        "    <when test='sortBy == \"playsCount_desc\"'>",
        "        ORDER BY rp.plays_count DESC",
        "    </when>",
        "    <when test='sortBy == \"likesCount_desc\"'>",
        "        ORDER BY rp.likes_count DESC",
        "    </when>",
        "    <when test='sortBy == \"isFeatured_desc_createdAt_desc\"'>",
        "        ORDER BY rp.is_featured DESC, rp.created_at DESC",
        "    </when>",
        "    <when test='sortBy == \"commentsCount_desc\"'>",
        "        ORDER BY rp.comments_count DESC",
        "    </when>",
        "    <otherwise>",
        "        ORDER BY rp.created_at DESC",
        "    </otherwise>",
        "</choose>",
        "</script>"
    })
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
    @Select({
        "SELECT ",
        "    rp.id, rp.title, rp.description, rp.audio_url as audioUrl, ",
        "    rp.cover_image_url as coverImageUrl, rp.category_id as categoryId, ",
        "    c.name as categoryName, rp.artist_narrator as artistNarrator, ",
        "    rp.album, rp.duration_seconds as durationSeconds, rp.tags, ",
        "    rp.publication_date as publicationDate, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, rp.comments_count as commentsCount, ",
        "    rp.is_featured as isFeatured, rp.status, ",
        "    rp.created_at as createdAt, rp.updated_at as updatedAt ",
        "FROM radio_programs rp ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE rp.id = #{programId} AND rp.status = 'published'"
    })
    RadioProgramDTO selectProgramDetailById(@Param("programId") Integer programId);

    /**
     * 增加播放次数
     * @param programId 节目ID
     * @return 影响行数
     */
    @org.apache.ibatis.annotations.Update("UPDATE radio_programs SET plays_count = plays_count + 1 WHERE id = #{programId}")
    int incrementPlaysCount(@Param("programId") Integer programId);

    /**
     * 增加喜欢次数
     * @param programId 节目ID
     * @return 影响行数
     */
    @org.apache.ibatis.annotations.Update("UPDATE radio_programs SET likes_count = likes_count + 1 WHERE id = #{programId}")
    int incrementLikesCount(@Param("programId") Integer programId);

    /**
     * 减少喜欢次数
     * @param programId 节目ID
     * @return 影响行数
     */
    @org.apache.ibatis.annotations.Update("UPDATE radio_programs SET likes_count = likes_count - 1 WHERE id = #{programId} AND likes_count > 0")
    int decrementLikesCount(@Param("programId") Integer programId);

    /**
     * 增加评论次数
     * @param programId 节目ID
     * @return 影响行数
     */
    @org.apache.ibatis.annotations.Update("UPDATE radio_programs SET comments_count = comments_count + 1 WHERE id = #{programId}")
    int incrementCommentsCount(@Param("programId") Integer programId);

    /**
     * 减少评论次数
     * @param programId 节目ID
     * @return 影响行数
     */
    @org.apache.ibatis.annotations.Update("UPDATE radio_programs SET comments_count = comments_count - 1 WHERE id = #{programId} AND comments_count > 0")
    int decrementCommentsCount(@Param("programId") Integer programId);

    /**
     * 获取热门节目（按播放次数排序）- 旧版本，保留兼容性
     * @param page 分页对象
     * @param limit 限制数量
     * @return 热门节目列表
     */
    @Select({
        "SELECT ",
        "    rp.id, rp.title, rp.cover_image_url as coverImageUrl, ",
        "    rp.artist_narrator as artistNarrator, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, c.name as categoryName ",
        "FROM radio_programs rp ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE rp.status = 'published' ",
        "ORDER BY rp.plays_count DESC ",
        "LIMIT #{limit}"
    })
    IPage<RadioProgramDTO> selectHotProgramsByPlays(
        Page<RadioProgramDTO> page,
        @Param("limit") Integer limit
    );

    /**
     * 获取精选节目
     * @param page 分页对象
     * @return 精选节目列表
     */
    @Select({
        "SELECT ",
        "    rp.id, rp.title, rp.cover_image_url as coverImageUrl, ",
        "    rp.artist_narrator as artistNarrator, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, c.name as categoryName ",
        "FROM radio_programs rp ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE rp.status = 'published' AND rp.is_featured = 1 ",
        "ORDER BY rp.created_at DESC"
    })
    IPage<RadioProgramDTO> selectFeaturedPrograms(Page<RadioProgramDTO> page);

    /**
     * 搜索节目
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 节目列表
     */
    @Select({
        "<script>",
        "SELECT ",
        "    rp.id, rp.title, rp.description, rp.audio_url as audioUrl, ",
        "    rp.cover_image_url as coverImageUrl, rp.artist_narrator as artistNarrator, ",
        "    rp.duration_seconds as durationSeconds, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, rp.comments_count as commentsCount, ",
        "    rp.tags, rp.is_featured as isFeatured, rp.status, ",
        "    rp.created_at as createdAt, rp.updated_at as updatedAt, ",
        "    c.name as categoryName ",
        "FROM radio_programs rp ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE rp.status = 'published' ",
        "<if test='keyword != null and keyword != \"\"'>",
        "    AND (",
        "        rp.title LIKE CONCAT('%', #{keyword}, '%') ",
        "        OR rp.description LIKE CONCAT('%', #{keyword}, '%') ",
        "        OR rp.artist_narrator LIKE CONCAT('%', #{keyword}, '%') ",
        "        OR rp.tags LIKE CONCAT('%', #{keyword}, '%') ",
        "        OR c.name LIKE CONCAT('%', #{keyword}, '%')",
        "    )",
        "</if>",
        "ORDER BY rp.created_at DESC",
        "</script>"
    })
    IPage<RadioProgramDTO> searchPrograms(Page<RadioProgramDTO> page, @Param("keyword") String keyword);

    /**
     * 获取热门节目列表（按热门度分数排序）
     * @param page 分页对象
     * @return 热门节目列表
     */
    @Select({
        "SELECT ",
        "    rp.id, rp.title, rp.description, rp.audio_url as audioUrl, ",
        "    rp.cover_image_url as coverImageUrl, rp.artist_narrator as artistNarrator, ",
        "    rp.duration_seconds as durationSeconds, rp.plays_count as playsCount, ",
        "    rp.likes_count as likesCount, rp.comments_count as commentsCount, ",
        "    rp.tags, rp.is_featured as isFeatured, rp.status, ",
        "    rp.created_at as createdAt, rp.updated_at as updatedAt, ",
        "    c.name as categoryName, ",
        "    (rp.likes_count * 3.0 + rp.comments_count * 5.0 + rp.plays_count * 1.0) as hotScore ",
        "FROM radio_programs rp ",
        "LEFT JOIN categories c ON rp.category_id = c.id ",
        "WHERE rp.status = 'published' ",
        "ORDER BY hotScore DESC, rp.created_at DESC"
    })
    IPage<RadioProgramDTO> selectHotPrograms(Page<RadioProgramDTO> page);

    /**
     * 获取热门节目列表（带排名）
     * @param page 分页对象
     * @return 热门节目列表
     */
    @Select({
        "SELECT ",
        "    ranked_programs.*, ",
        "    @row_number := @row_number + 1 as hotRank ",
        "FROM (",
        "    SELECT ",
        "        rp.id, rp.title, rp.description, rp.audio_url as audioUrl, ",
        "        rp.cover_image_url as coverImageUrl, rp.artist_narrator as artistNarrator, ",
        "        rp.duration_seconds as durationSeconds, rp.plays_count as playsCount, ",
        "        rp.likes_count as likesCount, rp.comments_count as commentsCount, ",
        "        rp.tags, rp.is_featured as isFeatured, rp.status, ",
        "        rp.created_at as createdAt, rp.updated_at as updatedAt, ",
        "        c.name as categoryName, ",
        "        (rp.likes_count * 3.0 + rp.comments_count * 5.0 + rp.plays_count * 1.0) as hotScore ",
        "    FROM radio_programs rp ",
        "    LEFT JOIN categories c ON rp.category_id = c.id ",
        "    WHERE rp.status = 'published' ",
        "    ORDER BY hotScore DESC, rp.created_at DESC ",
        "    LIMIT #{page.size} OFFSET #{page.offset}",
        ") ranked_programs ",
        "CROSS JOIN (SELECT @row_number := #{page.offset}) r ",
        "ORDER BY hotScore DESC"
    })
    List<RadioProgramDTO> selectHotProgramsWithRank(Page<RadioProgramDTO> page);
}
