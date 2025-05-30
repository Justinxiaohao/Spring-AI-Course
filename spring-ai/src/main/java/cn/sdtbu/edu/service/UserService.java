package cn.sdtbu.edu.service;

import cn.sdtbu.edu.dto.*;
import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.mapper.CommentMapper;
import cn.sdtbu.edu.mapper.PlaylistMapper;
import cn.sdtbu.edu.mapper.UserMapper;
import cn.sdtbu.edu.utils.EncryptionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author Wyh
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private CommentMapper commentMapper;

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

    /**
     * 获取用户个人资料
     * @param userId 用户ID
     * @return 用户个人资料
     */
    public UserProfileDTO getUserProfile(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

        UserProfileDTO profile = userMapper.selectUserProfile(userId);
        if (profile == null) {
            throw new RuntimeException("用户不存在");
        }

        return profile;
    }

    /**
     * 更新用户个人资料
     * @param userId 用户ID
     * @param request 更新请求
     * @return 更新后的用户资料
     */
    @Transactional(rollbackFor = Exception.class)
    public UserProfileDTO updateUserProfile(Integer userId, UpdateUserProfileRequest request) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (request == null) {
            throw new IllegalArgumentException("更新请求不能为空");
        }

        // 检查用户是否存在
        User existingUser = userMapper.selectById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否重复（如果要更新用户名）
        if (StringUtils.hasText(request.getUsername())) {
            if (request.getUsername().length() > 50) {
                throw new IllegalArgumentException("用户名不能超过50个字符");
            }
            int usernameExists = userMapper.checkUsernameExists(request.getUsername(), userId);
            if (usernameExists > 0) {
                throw new RuntimeException("用户名已存在，请使用其他用户名");
            }
        }

        try {
            // 更新用户信息
            User user = new User();
            user.setId(userId);
            if (StringUtils.hasText(request.getUsername())) {
                user.setUsername(request.getUsername());
            }
            if (request.getAvatar() != null) {
                user.setAvatar(request.getAvatar());
            }
            if (request.getBio() != null) {
                user.setBio(request.getBio());
            }

            int result = userMapper.updateById(user);
            if (result <= 0) {
                throw new RuntimeException("更新用户资料失败");
            }

            // 返回更新后的用户资料
            return userMapper.selectUserProfile(userId);
        } catch (Exception e) {
            throw new RuntimeException("更新用户资料失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户喜欢的节目列表
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 节目列表
     */
    public PageResult<RadioProgramDTO> getUserLikedPrograms(Integer userId, Integer page, Integer limit) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        // 创建分页对象
        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<RadioProgramDTO> result = userMapper.selectUserLikedPrograms(pageObj, userId);

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 获取用户的歌单列表
     * @param userId 用户ID
     * @return 歌单列表
     */
    public List<PlaylistDTO> getUserPlaylists(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }

        return playlistMapper.selectUserPlaylists(userId);
    }

    /**
     * 获取用户的评论列表
     * @param userId 用户ID
     * @param page 页码
     * @param limit 每页大小
     * @return 评论列表
     */
    public PageResult<CommentDTO> getUserComments(Integer userId, Integer page, Integer limit) {
        // 参数校验
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        // 创建分页对象
        Page<CommentDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<CommentDTO> result = commentMapper.selectUserComments(pageObj, userId);

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 检查用户是否存在
     * @param userId 用户ID
     * @return 是否存在
     */
    public boolean checkUserExists(Integer userId) {
        if (userId == null || userId <= 0) {
            return false;
        }

        try {
            User user = userMapper.selectById(userId);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据邮箱获取用户个人资料
     * @param email 用户邮箱
     * @return 用户个人资料
     */
    public UserProfileDTO getUserProfileByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }

        // 先根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        // 获取用户详细资料
        UserProfileDTO profile = userMapper.selectUserProfile(user.getId());
        if (profile == null) {
            throw new RuntimeException("用户资料获取失败");
        }

        return profile;
    }

    /**
     * 根据邮箱更新用户个人资料
     * @param email 用户邮箱
     * @param request 更新请求
     * @return 更新后的用户资料
     */
    @Transactional(rollbackFor = Exception.class)
    public UserProfileDTO updateUserProfileByEmail(String email, UpdateUserProfileRequest request) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (request == null) {
            throw new IllegalArgumentException("更新请求不能为空");
        }

        // 先根据邮箱查找用户
        User existingUser = userMapper.findByEmail(email);
        if (existingUser == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        Integer userId = existingUser.getId();

        // 检查用户名是否重复（如果要更新用户名）
        if (StringUtils.hasText(request.getUsername())) {
            if (request.getUsername().length() > 50) {
                throw new IllegalArgumentException("用户名不能超过50个字符");
            }
            int usernameExists = userMapper.checkUsernameExists(request.getUsername(), userId);
            if (usernameExists > 0) {
                throw new RuntimeException("用户名已存在，请使用其他用户名");
            }
        }

        try {
            // 更新用户信息
            User user = new User();
            user.setId(userId);
            if (StringUtils.hasText(request.getUsername())) {
                user.setUsername(request.getUsername());
            }
            if (request.getAvatar() != null) {
                user.setAvatar(request.getAvatar());
            }
            if (request.getBio() != null) {
                user.setBio(request.getBio());
            }

            int result = userMapper.updateById(user);
            if (result <= 0) {
                throw new RuntimeException("更新用户资料失败");
            }

            // 返回更新后的用户资料
            return userMapper.selectUserProfile(userId);
        } catch (Exception e) {
            throw new RuntimeException("更新用户资料失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据邮箱获取用户喜欢的节目列表
     * @param email 用户邮箱
     * @param page 页码
     * @param limit 每页大小
     * @return 节目列表
     */
    public PageResult<RadioProgramDTO> getUserLikedProgramsByEmail(String email, Integer page, Integer limit) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        // 先根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        // 创建分页对象
        Page<RadioProgramDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<RadioProgramDTO> result = userMapper.selectUserLikedPrograms(pageObj, user.getId());

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }

    /**
     * 根据邮箱获取用户的歌单列表
     * @param email 用户邮箱
     * @return 歌单列表
     */
    public List<PlaylistDTO> getUserPlaylistsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }

        // 先根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        return playlistMapper.selectUserPlaylists(user.getId());
    }

    /**
     * 根据邮箱获取用户的评论列表
     * @param email 用户邮箱
     * @param page 页码
     * @param limit 每页大小
     * @return 评论列表
     */
    public PageResult<CommentDTO> getUserCommentsByEmail(String email, Integer page, Integer limit) {
        // 参数校验
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (limit == null || limit < 1 || limit > 50) {
            limit = 10;
        }

        // 先根据邮箱查找用户
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("邮箱地址不存在");
        }

        // 创建分页对象
        Page<CommentDTO> pageObj = new Page<>(page, limit);

        // 执行查询
        IPage<CommentDTO> result = commentMapper.selectUserComments(pageObj, user.getId());

        // 转换为自定义分页结果
        return PageResult.of(
            result.getRecords(),
            result.getTotal(),
            result.getCurrent(),
            result.getSize()
        );
    }
}
