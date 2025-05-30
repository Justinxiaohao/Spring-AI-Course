package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一API响应格式
 * @author Wyh
 * @param <T> 数据类型
 */
@Getter
@Setter
public class ApiResponse<T> {
    
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 无参构造函数
     */
    public ApiResponse() {
    }

    /**
     * 构造函数
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @param success 是否成功
     */
    public ApiResponse(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /**
     * 成功响应（带数据）
     * @param data 响应数据
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data, true);
    }

    /**
     * 成功响应（带数据和消息）
     * @param data 响应数据
     * @param message 响应消息
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data, true);
    }

    /**
     * 成功响应（仅消息）
     * @param message 响应消息
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null, true);
    }

    /**
     * 失败响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null, false);
    }

    /**
     * 失败响应（带状态码）
     * @param code 状态码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, false);
    }

    /**
     * 参数错误响应
     * @param message 错误消息
     * @param <T> 数据类型
     * @return API响应
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null, false);
    }
}
