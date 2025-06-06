package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 分类Mapper接口
 * @author Wyh
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 获取所有分类列表
     * @return 分类列表
     */
    List<Category> selectAllCategories();

    /**
     * 根据名称查找分类
     * @param name 分类名称
     * @return 分类信息
     */
    Category selectByName(String name);
}
