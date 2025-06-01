package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.PageResult;
import cn.sdtbu.edu.dto.RadioProgramDTO;
import cn.sdtbu.edu.entity.RadioProgram;
import cn.sdtbu.edu.mapper.RadioProgramMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 电台节目服务类
 * @author Wyh
 */
@Service
public class RadioProgramService extends ServiceImpl<RadioProgramMapper, RadioProgram> {

    @Autowired
    private RadioProgramMapper radioProgramMapper;

    /**
     * 分页查询电台节目列表
     * @param page 页码（从1开始）
     * @param limit 每页大小
     * @param sortBy 排序方式
     * @param categoryId 分类ID（可选）
     * @param tag 标签（可选）
     * @return 分页结果
     */
    public PageResult<RadioProgramDTO> getPrograms(Integer page, Integer limit, 
                                                  String sortBy, Integer categoryId, String tag) {
        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 100) {
            limit = 10; // 默认每页10条，最大100条
        }
        if (!StringUtils.hasText(sortBy)) {
            sortBy = "createdAt_desc"; // 默认按创建时间倒序
        }

        // 验证排序参数
        if (!isValidSortBy(sortBy)) {
            sortBy = "createdAt_desc";
        }

        // 创建分页对象
        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<RadioProgramDTO> result = radioProgramMapper.selectProgramsWithCategory(
            pageObj, categoryId, tag, sortBy
        );

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 根据ID获取节目详情
     * @param programId 节目ID
     * @return 节目详情
     */
    public RadioProgramDTO getProgramDetail(Integer programId) {
        if (programId == null || programId <= 0) {
            throw new IllegalArgumentException("节目ID不能为空或无效");
        }
        
        RadioProgramDTO program = radioProgramMapper.selectProgramDetailById(programId);
        if (program == null) {
            throw new RuntimeException("节目不存在或已下架");
        }
        
        return program;
    }

    /**
     * 增加播放次数
     * @param programId 节目ID
     * @return 是否成功
     */
    public boolean incrementPlaysCount(Integer programId) {
        if (programId == null || programId <= 0) {
            return false;
        }
        
        try {
            int result = radioProgramMapper.incrementPlaysCount(programId);
            return result > 0;
        } catch (Exception e) {
            // 记录日志但不抛出异常，播放统计失败不应影响正常播放
            System.err.println("增加播放次数失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取热门节目（按热门度分数排序）
     * @param page 页码
     * @param limit 每页大小
     * @return 热门节目列表
     */
    public PageResult<RadioProgramDTO> getHotPrograms(Integer page, Integer limit) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);
        IPage<RadioProgramDTO> result = radioProgramMapper.selectHotPrograms(pageObj);

        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 获取热门节目（带排名信息）
     * @param page 页码
     * @param limit 每页大小
     * @return 热门节目列表（包含排名）
     */
    public PageResult<RadioProgramDTO> getHotProgramsWithRank(Integer page, Integer limit) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);

        // 使用带排名的查询方法
        List<RadioProgramDTO> programs = radioProgramMapper.selectHotProgramsWithRank(pageObj);

        // 计算总数（这里简化处理，实际应该有单独的count查询）
        long total = programs.size();

        return PageResult.of(
            programs,
            total,
            (long) page,
            (long) limit
        );
    }

    /**
     * 获取精选节目
     * @param page 页码
     * @param limit 每页大小
     * @return 精选节目列表
     */
    public PageResult<RadioProgramDTO> getFeaturedPrograms(Integer page, Integer limit) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);
        IPage<RadioProgramDTO> result = radioProgramMapper.selectFeaturedPrograms(pageObj);

        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 验证排序参数是否有效
     * @param sortBy 排序参数
     * @return 是否有效
     */
    private boolean isValidSortBy(String sortBy) {
        if (!StringUtils.hasText(sortBy)) {
            return false;
        }
        
        // 允许的排序方式
        String[] validSortOptions = {
            "createdAt_desc",           // 最新
            "playsCount_desc",          // 最热（播放次数）
            "likesCount_desc",          // 最受欢迎（点赞次数）
            "isFeatured_desc_createdAt_desc", // 精选优先，然后按时间
            "commentsCount_desc"        // 评论最多
        };
        
        for (String validOption : validSortOptions) {
            if (validOption.equals(sortBy)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 搜索节目
     * @param keyword 搜索关键词
     * @param page 页码
     * @param limit 每页大小
     * @return 搜索结果
     */
    public PageResult<RadioProgramDTO> searchPrograms(String keyword, Integer page, Integer limit) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10; // 默认每页10条，最大50条
        }

        // 创建分页对象
        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);

        // 执行搜索
        IPage<RadioProgramDTO> result = radioProgramMapper.searchPrograms(pageObj, keyword);

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }
}
