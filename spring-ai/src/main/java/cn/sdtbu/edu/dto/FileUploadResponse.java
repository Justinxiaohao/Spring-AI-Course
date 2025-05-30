package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传响应DTO
 * @author Wyh
 */
@Getter
@Setter
public class FileUploadResponse {
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 文件访问URL
     */
    private String fileUrl;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 上传时间戳
     */
    private Long uploadTime;

    /**
     * 无参构造函数
     */
    public FileUploadResponse() {
    }

    /**
     * 带参构造函数
     * @param fileName 文件名
     * @param fileUrl 文件访问URL
     * @param fileSize 文件大小
     * @param fileType 文件类型
     * @param uploadTime 上传时间戳
     */
    public FileUploadResponse(String fileName, String fileUrl, Long fileSize, String fileType, Long uploadTime) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.uploadTime = uploadTime;
    }
}
