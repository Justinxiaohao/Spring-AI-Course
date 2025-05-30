package cn.sdtbu.edu.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 文件验证工具类
 * @author Wyh
 */
public class FileValidationUtil {
    
    /**
     * 允许的图片文件类型
     */
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif"
    );
    
    /**
     * 允许的图片文件扩展名
     */
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
        ".jpg", ".jpeg", ".png", ".gif"
    );
    
    /**
     * 最大文件大小（5MB）
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    /**
     * 验证上传的文件是否为有效的图片文件
     * @param file 上传的文件
     * @return 验证结果
     */
    public static ValidationResult validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ValidationResult(false, "文件不能为空");
        }
        
        // 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            return new ValidationResult(false, "文件大小不能超过5MB");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            return new ValidationResult(false, "只支持JPG、PNG、GIF格式的图片文件");
        }
        
        // 验证文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return new ValidationResult(false, "文件名不能为空");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            return new ValidationResult(false, "只支持JPG、PNG、GIF格式的图片文件");
        }
        
        return new ValidationResult(true, "文件验证通过");
    }
    
    /**
     * 获取文件扩展名
     * @param filename 文件名
     * @return 扩展名（包含点号）
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }
    
    /**
     * 生成安全的文件名
     * @param originalFilename 原始文件名
     * @return 安全的文件名
     */
    public static String generateSafeFilename(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "unknown_" + System.currentTimeMillis();
        }
        
        String extension = getFileExtension(originalFilename);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = String.valueOf((int)(Math.random() * 1000));
        
        return "avatar_" + timestamp + "_" + randomSuffix + extension;
    }
    
    /**
     * 验证结果内部类
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
