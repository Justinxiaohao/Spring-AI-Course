package cn.sdtbu.edu.controller;

import cn.sdtbu.edu.dto.ApiResponse;
import cn.sdtbu.edu.dto.FileUploadResponse;
import cn.sdtbu.edu.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传控制器
 * @author Wyh
 */
@CrossOrigin
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    /**
     * 上传头像
     * POST /api/upload/avatar
     * 
     * @param file 上传的头像文件
     * @return 上传结果
     */
    @PostMapping("/avatar")
    public ApiResponse<FileUploadResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件参数
            if (file == null || file.isEmpty()) {
                return ApiResponse.badRequest("请选择要上传的文件");
            }
            
            // 上传文件
            FileUploadResponse response = fileUploadService.uploadAvatar(file);
            
            return ApiResponse.success(response, "头像上传成功");
            
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (IOException e) {
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("系统错误: " + e.getMessage());
        }
    }
    
    /**
     * 删除头像
     * DELETE /api/upload/avatar/{filename}
     * 
     * @param filename 要删除的文件名
     * @return 删除结果
     */
    @DeleteMapping("/avatar/{filename}")
    public ApiResponse<Boolean> deleteAvatar(@PathVariable String filename) {
        try {
            boolean deleted = fileUploadService.deleteAvatar(filename);
            
            if (deleted) {
                return ApiResponse.success(true, "头像删除成功");
            } else {
                return ApiResponse.error("头像删除失败，文件可能不存在");
            }
            
        } catch (Exception e) {
            return ApiResponse.error("删除头像时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 检查文件是否存在
     * GET /api/upload/avatar/{filename}/exists
     * 
     * @param filename 文件名
     * @return 文件是否存在
     */
    @GetMapping("/avatar/{filename}/exists")
    public ApiResponse<Boolean> checkAvatarExists(@PathVariable String filename) {
        try {
            boolean exists = fileUploadService.fileExists(filename);
            return ApiResponse.success(exists, exists ? "文件存在" : "文件不存在");
            
        } catch (Exception e) {
            return ApiResponse.error("检查文件时发生错误: " + e.getMessage());
        }
    }
}
