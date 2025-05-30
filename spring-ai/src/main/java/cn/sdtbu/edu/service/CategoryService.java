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

    /**
     * 创建新分类
     * @param name 分类名称
     * @param description 分类描述
     * @return 创建的分类
     */
    public Category createCategory(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }

        // 检查分类名称是否已存在
        Category existing = getCategoryByName(name.trim());
        if (existing != null) {
            throw new RuntimeException("分类名称已存在");
        }

        Category category = new Category(name.trim(), description);
        int result = categoryMapper.insert(category);
        
        if (result > 0) {
            return category;
        } else {
            throw new RuntimeException("创建分类失败");
        }
    }
}
