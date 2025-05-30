package cn.sdtbu.edu.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 分页结果封装类
 * @author Wyh
 * @param <T> 数据类型
 */
@Getter
@Setter
public class PageResult<T> {
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Long current;
    
    /**
     * 每页大小
     */
    private Long size;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 无参构造函数
     */
    public PageResult() {
    }

    /**
     * 构造函数
     * @param records 数据列表
     * @param total 总记录数
     * @param current 当前页码
     * @param size 每页大小
     */
    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size; // 计算总页数
        this.hasPrevious = current > 1;
        this.hasNext = current < pages;
    }

    /**
     * 静态工厂方法
     * @param records 数据列表
     * @param total 总记录数
     * @param current 当前页码
     * @param size 每页大小
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        return new PageResult<>(records, total, current, size);
    }
}
