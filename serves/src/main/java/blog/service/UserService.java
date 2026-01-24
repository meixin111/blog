package blog.service;

import blog.dto.LoginDTO;
import blog.dto.LoginResponse;
import blog.entity.User;
import blog.repository.UserRepository;
import blog.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginDTO dto) {
        try {
            // 查找用户
            User user = userRepository.findByUsername(dto.getAccount())
                    .orElseThrow(() -> new RuntimeException("账号不存在"));

            // 验证密码（明文比较）
            if (!dto.getPassword().equals(user.getPassword())) {
                throw new RuntimeException("密码错误");
            }

            // 生成token
            String token = jwtUtil.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .account(user.getUsername())
                    .id(user.getId())
                    .build();

        } catch (Exception e) {
            // 打印错误日志
            System.err.println("登录失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("登录失败: " + e.getMessage());
        }
    }

    public void initAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123456tcy");
            admin.setNickname("管理员");
            admin.setEmail("admin@blog.com");
            userRepository.save(admin);
            System.out.println("✅ 已创建管理员账号：admin / 123456tcy");
        }
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        return userRepository.save(user);
    }
}