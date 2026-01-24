package blog.config;

import blog.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 配置CORS
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    // 允许的前端地址
                    config.setAllowedOrigins(Arrays.asList(
                            "http://localhost:5173",  // Vue默认端口
                            "http://localhost:8080",  // 后端自身
                            "http://localhost:8081"   // 其他前端端口
                    ));

                    // 允许的HTTP方法
                    config.setAllowedMethods(Arrays.asList(
                            "GET", "POST", "PUT", "DELETE", "OPTIONS"
                    ));

                    // 允许的请求头
                    config.setAllowedHeaders(Arrays.asList("*"));

                    // 允许携带凭证（cookies）
                    config.setAllowCredentials(true);

                    // 预检请求缓存时间（秒）
                    config.setMaxAge(3600L);

                    return config;
                }))

                // 2. 禁用CSRF（因为使用JWT）
                .csrf(csrf -> csrf.disable())

                // 3. 配置权限规则
                .authorizeHttpRequests(auth -> auth
                        // 允许OPTIONS预检请求（必须）
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 公开接口（不需要登录）
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/blog/search").permitAll()
                        .requestMatchers("/blog/detail").permitAll()
                        .requestMatchers("/category/list").permitAll()
                        .requestMatchers("/category/detail").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/debug/**").permitAll()

                        // 需要认证的接口（需要JWT）
                        .requestMatchers("/blog/_token/add").authenticated()
                        .requestMatchers("/blog/_token/update").authenticated()
                        .requestMatchers("/blog/_token/delete").authenticated()
                        .requestMatchers("/category/_token/add").authenticated()
                        .requestMatchers("/category/_token/update").authenticated()
                        .requestMatchers("/category/_token/delete").authenticated()

                        // 其他请求默认需要认证
                        .anyRequest().authenticated()
                )

                // 4. 配置无状态Session（使用JWT）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}