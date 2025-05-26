package cn.sdtbu.edu.controller;


import cn.sdtbu.edu.entity.User;
import cn.sdtbu.edu.service.UserService;
import cn.sdtbu.edu.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationCodeService verificationCodeService;


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        try {
            User user = userService.register(request.get("email"), request.get("password"));
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            User user = userService.login(request.get("email"), request.get("password"));
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("data", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login/captcha")
    public ResponseEntity<Map<String, Object>> loginWithCaptcha(@RequestBody Map<String, String> request) {
        try {
            boolean isValid = verificationCodeService.validateCode(request.get("email"), request.get("captcha"));
            Map<String, Object> response = new HashMap<>();
            if (isValid) {
                User user = userService.findByEmail(request.get("email"));
                if (user != null) {
                    response.put("success", true);
                    response.put("message", "登录成功");
                    response.put("data", user);
                    return ResponseEntity.ok(response);
                } else {
                    response.put("success", false);
                    response.put("message", "用户不存在");
                    response.put("data", null);
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "验证码无效或已过期");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/check-user")
    public ResponseEntity<Map<String, Object>> checkUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");

        if (email != null && !email.isEmpty()) {
            // 查询用户是否存在
            User user = userService.findByEmail(email);
            if (user != null) {
                //用户存在
                response.put("exists", true);
            } else {
                response.put("notexists", false);
            }
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "邮箱不能为空");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<User> getUserInfo(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateUserInfo(@RequestParam String email,
                                                 @RequestParam String nickname,
                                                 @RequestParam String bio,
                                                 @RequestParam(required = false) MultipartFile avatar) {
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户未找到");
            }

            // 更新用户基本信息
            user.setUsername(nickname);
            user.setBio(bio);

            // 保存更新后的用户信息
            userService.updateUser(user);

            return ResponseEntity.ok("用户信息更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("保存个人信息失败");
        }
    }
}


