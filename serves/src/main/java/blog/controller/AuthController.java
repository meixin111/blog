package blog.controller;

import blog.dto.LoginDTO;
import blog.dto.LoginResponse;
import blog.dto.ResponseResult;
import blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseResult<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            LoginResponse response = userService.login(loginDTO);
            return ResponseResult.success(response);
        } catch (RuntimeException e) {
            return ResponseResult.unauthorized(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("登录失败: " + e.getMessage());
        }
    }
}