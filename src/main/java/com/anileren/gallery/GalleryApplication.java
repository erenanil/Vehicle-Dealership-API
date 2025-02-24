package com.anileren.gallery;

import java.util.Date;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.anileren.enums.Role;
import com.anileren.model.User;
import com.anileren.repository.UserRepository;

@SpringBootApplication
@EntityScan(basePackages = {"com.anileren"})
@ComponentScan(basePackages = {"com.anileren"})
@EnableJpaRepositories(basePackages = {"com.anileren"})
public class GalleryApplication {
	public static void main(String[] args) {
		SpringApplication.run(GalleryApplication.class, args);
	}

	@Bean
	CommandLineRunner adminCreator(UserRepository userRepository){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				User admin = new User();
				admin.setCreateTime(new Date());
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setRoles(Set.of(Role.ADMIN));
				userRepository.save(admin);
			}
			
		};
	}
}
