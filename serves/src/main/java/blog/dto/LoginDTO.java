package blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 12, message = "账号长度3-12位")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 18, message = "密码长度6-18位")
    private String password;
}