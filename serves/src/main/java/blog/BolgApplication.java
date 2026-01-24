
package blog;

import blog.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BolgApplication {
    public static void main(String[] args) {
        SpringApplication.run(BolgApplication.class, args);
    }

}