package cn.sdtbu.edu.controller;


import cn.sdtbu.edu.entity.VerificationCode;
import cn.sdtbu.edu.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/code")
public class VerificationCodeController {
    @Autowired
    private VerificationCodeService verificationCodeService;


    @PostMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendCode(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            VerificationCode code = verificationCodeService.generateCode(request.get("email"));
            // 这里可以添加发送验证码到用户邮箱的逻辑
            response.put("success", true);
            response.put("message", "验证码已发送");
            // 不需要返回数据时设置为null
            response.put("data", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isValid = verificationCodeService.validateCode(request.get("email"), request.get("code"));
            if (isValid) {
                response.put("success", true);
                response.put("message", "验证码验证成功");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "验证码无效或已过期");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
