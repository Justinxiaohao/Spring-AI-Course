package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.ApiResponse;
import cn.sdtbu.edu.entity.Category;
import cn.sdtbu.edu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 * @author Wyh
 */
@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类列表
     * GET /api/categories
     * 
     * @return 分类列表
     */
    @GetMapping
    public ApiResponse<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ApiResponse.success(categories, "获取分类列表成功");
        } catch (Exception e) {
            return ApiResponse.error("获取分类列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取分类详情
     * GET /api/categories/{categoryId}
     * 
     * @param categoryId 分类ID
     * @return 分类详情
     */
    @GetMapping("/{categoryId}")
    public ApiResponse<Category> getCategoryById(@PathVariable Integer categoryId) {
        try {
            Category category = categoryService.getById(categoryId);
            if (category == null) {
                return ApiResponse.error(404, "分类不存在");
            }
            return ApiResponse.success(category, "获取分类详情成功");
        } catch (Exception e) {
            return ApiResponse.error("获取分类详情失败: " + e.getMessage());
        }
    }
}
