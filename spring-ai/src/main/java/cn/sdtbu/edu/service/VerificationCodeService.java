package cn.sdtbu.edu.service;

import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.entity.VerificationCode;
import cn.sdtbu.edu.mapper.UserMapper;
import cn.sdtbu.edu.mapper.VerificationCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

/**
 * @author Wyh
 */
@Service
public class VerificationCodeService {
    @Autowired
    private VerificationCodeMapper verificationCodeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 生成并发送验证码
     * @param email 用户的邮箱
     * @return 生成的验证码信息
     */
// VerificationCodeService.java
    public VerificationCode generateCode(String email) {
        // 生成验证码
        String code = String.format("%06d", new Random().nextInt(999999));

        // 创建验证码对象并保存
        VerificationCode verificationCode = new VerificationCode();
        // 设置用户邮箱
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setCreatedAt(Timestamp.from(Instant.now()));
        //设置验证码过期时间为5分钟
        verificationCode.setExpiresAt(Timestamp.from(Instant.now().plusSeconds(300)));
        //保存验证码到数据库
        verificationCodeMapper.insert(verificationCode);

        // 发送验证码到用户邮箱
        sendEmail(email, code);

        return verificationCode;
    }    public boolean validateCode(String email, String code) {
        // 查找用户的最新验证码
        VerificationCode verificationCode = verificationCodeMapper.findTopByEmailOrderByCreatedAtDesc(email);
        if (verificationCode == null || verificationCode.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            // 如果验证码已过期，则删除它
            if (verificationCode != null) {
                verificationCodeMapper.deleteById(verificationCode.getId());
            }
            return false;
        }
        // 校验验证码是否正确
        return verificationCode.getCode().equals(code);
    }
    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @param code 生成的验证码
     */
    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1418661311@qq.com");
        message.setTo(to);
        message.setSubject("心理治愈电台登录验证码");
        message.setText("即刻登录,开始电台新体验,您的验证码是：" + code + "，有效期为5分钟。请尽快使用。请妥善保管,请勿泄露给他人");
        mailSender.send(message);
    }

    /**
     * 注册新用户，验证验证码是否有效，保存用户信息
     * @param email 用户的邮箱
     * @param password 用户的密码
     * @param code 用户输入的验证码
     * @return 注册成功的用户对象
     */    public User registerUser(String email, String password, String code) {
        // 检查用户是否已存在
        User existingUser = userMapper.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("用户已存在");
        }

        // 验证验证码
        boolean valid = validateCode(email, code);
        if (!valid) {
            throw new RuntimeException("验证码无效或已过期");
        }

        // 创建新用户并保存
        User user = new User();
        user.setEmail(email);
        // 加密密码后存储
        user.setPassword(password);
        userMapper.insert(user);

        // 注册成功后返回用户对象
        return user;
    }    @Scheduled(cron = "0 0 * * * ?")  // 每小时执行一次，具体时间根据需求设置
    public void cleanExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        // 删除所有过期验证码
        verificationCodeMapper.deleteByExpiresAtBefore(new Timestamp(currentTime));
        System.out.println("清理过期验证码完成");
    }
}
