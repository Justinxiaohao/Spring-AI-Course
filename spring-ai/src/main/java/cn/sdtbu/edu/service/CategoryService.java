package cn.sdtbu.edu.service;

import cn.sdtbu.edu.entity.Category;
import cn.sdtbu.edu.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务类
 * @author Wyh
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取所有分类列表
     * @return 分类列表
     */
    public List<Category> getAllCategories() {
        return categoryMapper.selectAllCategories();
    }

    /**
     * 根据名称查找分类
     * @param name 分类名称
     * @return 分类信息
     */
    public Category getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return categoryMapper.selectByName(name.trim());
    }

}
