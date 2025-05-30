package cn.sdtbu.edu.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * @author Wyh
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置MyBatis-Plus拦截器
     * 主要用于分页功能
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 添加分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型为MySQL
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 设置最大单页限制数量，默认500条，-1不受限制
        paginationInnerInterceptor.setMaxLimit(500L);
        // 溢出总页数后是否进行处理（默认不处理）
        paginationInnerInterceptor.setOverflow(false);
        
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        return interceptor;
    }
}
