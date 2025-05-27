package cn.sdtbu.edu.service;

import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.mapper.UserMapper;
import cn.sdtbu.edu.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Wyh
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User register(String email, String password) {

        //String encryptedPassword1 = EncryptionUtil.encrypt(password);
        System.out.println("原始密码：" + password);
        try {
            // 加密密码
            String encryptedPassword = EncryptionUtil.encrypt(password);


            // 生成 username，假设使用邮箱前缀
            String username = email.split("@")[0];

            // 创建用户对象
            User user = new User();
            user.setEmail(email);
            // 存储加密后的密码
            user.setPassword(encryptedPassword);

            user.setUsername(username);
            user.setAvatar("/img/avatar0" + (new java.util.Random().nextInt(3) + 1) + ".png");
            user.setBio("这是"+username+"的心理音乐电台");
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());
            user.setCreatedAt(timestamp);
            return userMapper.insert(user) > 0 ? user : null;
        } catch (Exception e) {
            throw new RuntimeException("注册失败：密码加密出错", e);
        }
    }

    public User login(String email, String password) {
        try {
            User user = userMapper.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("邮箱或密码错误");
            }

            // 解密数据库中存储的密码并比对
            String decryptedPassword = EncryptionUtil.decrypt(user.getPassword());
            if (!password.equals(decryptedPassword)) {
                throw new RuntimeException("邮箱或密码错误");
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("登录失败：密码解密出错", e);
        }
    }    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
    public void updateUser(User user) {
        userMapper.updateById(user);
    }


}
