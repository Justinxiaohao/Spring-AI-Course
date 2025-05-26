package cn.sdtbu.edu;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import java.sql.Types;
import java.util.Collections;
/**
 * @author Wyh
 */
public class Gena{
    /**
     * MyBatis-Plus 代码生成器示例
     * 生成的代码将输出到指定目录，并使用 Freemarker 模板引擎
     */
    public static void main(String[] args) {
    FastAutoGenerator.create("jdbc:mysql://localhost:3306/users", "root", "wuyuhao123456#")
            .globalConfig(builder -> {
                builder.author("baomidou") // 设置作者
                        .enableSwagger() // 开启 swagger 模式
                        .outputDir("D:\\CodeProject\\javacode\\spring-ai\\src\\main\\java\\cn\\sdtbu\\edu\\"); // 指定输出目录
            })
            .dataSourceConfig(builder ->
                    builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                        int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                        if (typeCode == Types.SMALLINT) {
                            // 自定义类型转换
                            return DbColumnType.INTEGER;
                        }
                        return typeRegistry.getColumnType(metaInfo);
                    })
            )
            .dataSourceConfig(builder ->
                    builder.databaseQueryClass(SQLQuery.class)
                            .typeConvert(new MySqlTypeConvert())
                            .dbQuery(new MySqlQuery())
            )
            .packageConfig(builder ->
                    builder.parent("com.baomidou") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\CodeProject\\javacode\\spring-ai\\src\\main\\java\\cn\\sdtbu\\edu\\")) // 设置mapperXml生成路径
            )
            .strategyConfig(builder ->
                    builder.addInclude("users") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
            )
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();
}
}
