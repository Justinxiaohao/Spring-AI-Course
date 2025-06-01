package cn.sdtbu.edu.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.Date;

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

    /**
     * 配置字段自动填充处理器
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                Timestamp now = new Timestamp(new Date().getTime());

                // 插入时自动填充创建时间
                this.strictInsertFill(metaObject, "createdAt", Timestamp.class, now);
                this.strictInsertFill(metaObject, "addedAt", Timestamp.class, now);

                // 插入时自动填充更新时间
                this.strictInsertFill(metaObject, "updatedAt", Timestamp.class, now);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                Timestamp now = new Timestamp(new Date().getTime());

                // 更新时自动填充更新时间
                this.strictUpdateFill(metaObject, "updatedAt", Timestamp.class, now);
            }
        };
    }
}
