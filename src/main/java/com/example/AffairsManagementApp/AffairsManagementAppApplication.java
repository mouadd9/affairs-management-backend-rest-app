package com.example.AffairsManagementApp;
import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.entities.Role;
import com.example.AffairsManagementApp.mappers.RoleMapper;
import com.example.AffairsManagementApp.repositories.Userrepository;
import com.example.AffairsManagementApp.security.RsaKeyProperties;
import com.example.AffairsManagementApp.services.AgencyService;
import com.example.AffairsManagementApp.services.RoleService;
import com.example.AffairsManagementApp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;
@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@ComponentScan @AllArgsConstructor
public class AffairsManagementAppApplication {

	private final UserService userService;
    private final RoleService roleService;
	private final RoleMapper roleMapper;
	private final Userrepository userrepository;
	private final AgencyService agencyService;

	public static void main(String[] args) {
		SpringApplication.run(AffairsManagementAppApplication.class, args);
	}

	@Bean
	CommandLineRunner createInitialUser() {
		return args -> {
			roleService.addRole("ADMIN");
			roleService.addRole("AGENCY_EMPLOYEE");
			AgencyDTO agencyDTO = new AgencyDTO();
			agencyDTO.setAgency_code("agence1");
			agencyService.createAgency(agencyDTO);

			UserDTO userDTO = new UserDTO();
			userDTO.setUsername("Mouad");
			userDTO.setPassword("123");
			userDTO.setEmail("jshkjo");
			userDTO.setFirstName("test");
			userDTO.setLastName("test");
			int intUserId = 1;


// Convert int to Long
			Long userId = Long.valueOf(intUserId);

			userService.addAgencyEmployee(userDTO,userId );






			System.out.println("Default admin user created: admin");

		};
	}




}
