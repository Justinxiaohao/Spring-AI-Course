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
    VerificationCode findTopByEmailOrderByCreatedAtDesc(String email);

    /**
     * 删除过期的验证码
     */
    void deleteByExpiresAtBefore(Timestamp timestamp);
}
