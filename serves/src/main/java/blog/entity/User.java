package blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 12)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    private String email;
    private String avatar;
    private String nickname;

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;
}