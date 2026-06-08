package N18.haui.Pet_18;

import N18.haui.Pet_18.constant.RoleConstant;
import N18.haui.Pet_18.domain.entity.Role;
import N18.haui.Pet_18.domain.entity.User;
import N18.haui.Pet_18.repository.RoleRepository;
import N18.haui.Pet_18.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Pet18Application {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Pet18Application.class, args);
	}

    @Bean
	CommandLineRunner init() {
        return args -> {
            //init role
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(null, RoleConstant.ADMIN, "Role for admin"));
                roleRepository.save(new Role(null, RoleConstant.USER, "Role for user"));
                roleRepository.save(new Role(null, RoleConstant.STAFF, "Role for staff"));
            }
            //init admin
            if (userRepository.count() == 0) {
                Role role = roleRepository.findByName(RoleConstant.ADMIN).orElse(null);
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin@123"));
                admin.setRole(role);
                userRepository.save(admin);
            }
        };
    }
}
