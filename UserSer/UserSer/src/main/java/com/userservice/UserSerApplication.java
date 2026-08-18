package com.userservice;

import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableFeignClients
public class UserSerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserSerApplication.class, args);
        System.out.println("Runs Successfully");
	}



    @Bean
    public CommandLineRunner createAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@gmail.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                repo.save(admin);

                System.out.println("✅ Admin Created");
            }
        };
    }



}
