package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.FileUploadResponse;
import cn.sdtbu.edu.utils.FileValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 文件上传服务
 * @author Wyh
 */
@Service
public class FileUploadService {
    
    @Value("${file.upload.avatar-path}")
    private String avatarUploadPath;
    
    @Value("${file.upload.base-url}")
    private String baseUrl;
    
    /**
     * 上传头像文件
     * @param file 上传的文件
     * @return 文件上传响应
     * @throws IOException 文件操作异常
     */
    public FileUploadResponse uploadAvatar(MultipartFile file) throws IOException {
        // 验证文件
        FileValidationUtil.ValidationResult validationResult = FileValidationUtil.validateImageFile(file);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException(validationResult.getMessage());
        }
        
        // 确保上传目录存在
        createUploadDirectoryIfNotExists();
        
        // 生成安全的文件名
        String safeFilename = FileValidationUtil.generateSafeFilename(file.getOriginalFilename());
        
        // 构建文件保存路径
        Path uploadPath = Paths.get(avatarUploadPath);
        Path filePath = uploadPath.resolve(safeFilename);
        
        try {
            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 构建文件访问URL
            String fileUrl = baseUrl + "avatars/" + safeFilename;
            
            // 返回上传结果
            return new FileUploadResponse(
                safeFilename,
                fileUrl,
                file.getSize(),
                file.getContentType(),
                System.currentTimeMillis()
            );
            
        } catch (IOException e) {
            throw new IOException("文件保存失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除头像文件
     * @param filename 文件名
     * @return 是否删除成功
     */
    public boolean deleteAvatar(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        
        try {
            Path filePath = Paths.get(avatarUploadPath, filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * 检查文件是否存在
     * @param filename 文件名
     * @return 文件是否存在
     */
    public boolean fileExists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        
        Path filePath = Paths.get(avatarUploadPath, filename);
        return Files.exists(filePath);
    }
    
    /**
     * 创建上传目录（如果不存在）
     * @throws IOException 目录创建异常
     */
    private void createUploadDirectoryIfNotExists() throws IOException {
        Path uploadPath = Paths.get(avatarUploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }
    
    /**
     * 从URL中提取文件名
     * @param avatarUrl 头像URL
     * @return 文件名
     */
    public String extractFilenameFromUrl(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return null;
        }
        
        // 如果是相对路径，提取文件名
        if (avatarUrl.startsWith(baseUrl)) {
            String relativePath = avatarUrl.substring(baseUrl.length());
            if (relativePath.startsWith("avatars/")) {
                return relativePath.substring("avatars/".length());
            }
        }
        
        return null;
    }
}
