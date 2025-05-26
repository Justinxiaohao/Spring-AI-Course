package cn.sdtbu.edu.mapper;

import cn.sdtbu.edu.entity.VerificationCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;

@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCode> {

    /**
     * 根据邮箱查找最新的验证码
     */
    @Select("SELECT * FROM verification_codes WHERE email = #{email} ORDER BY created_at DESC LIMIT 1")
    VerificationCode findTopByEmailOrderByCreatedAtDesc(String email);

    /**
     * 删除过期的验证码
     */
    @Delete("DELETE FROM verification_codes WHERE expires_at < #{timestamp}")
    void deleteByExpiresAtBefore(Timestamp timestamp);
}
