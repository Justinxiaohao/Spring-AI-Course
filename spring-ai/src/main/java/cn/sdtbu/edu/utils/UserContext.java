package cn.sdtbu.edu.utils;

/**
 * 用户上下文工具类
 * 用于在请求处理过程中传递用户信息
 * 注意：这是一个简化的实现，实际项目中应该使用JWT或Session等更安全的方式
 * @author Wyh
 */
public class UserContext {
    
    private static final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();
    
    /**
     * 设置当前用户ID
     * @param userId 用户ID
     */
    public static void setCurrentUserId(Integer userId) {
        userIdHolder.set(userId);
    }
    
    /**
     * 获取当前用户ID
     * @return 用户ID，如果未设置则返回null
     */
    public static Integer getCurrentUserId() {
        return userIdHolder.get();
    }
    
    /**
     * 清除当前用户信息
     */
    public static void clear() {
        userIdHolder.remove();
    }
    
    /**
     * 检查是否有当前用户
     * @return 是否有当前用户
     */
    public static boolean hasCurrentUser() {
        return userIdHolder.get() != null;
    }
}
