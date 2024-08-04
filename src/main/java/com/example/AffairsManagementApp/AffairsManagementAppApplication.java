package com.example.AffairsManagementApp;
import com.example.AffairsManagementApp.mappers.RoleMapper;
import com.example.AffairsManagementApp.repositories.Userrepository;
import com.example.AffairsManagementApp.security.RsaKeyProperties;
import com.example.AffairsManagementApp.services.interfaces.AgencyService;
import com.example.AffairsManagementApp.services.interfaces.RoleService;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

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

	/*@Bean
	CommandLineRunner createInitialUser() {

		return args -> {
			roleService.addRole("ADMIN");
			roleService.addRole("AGENCY_EMPLOYEE");
			AgencyDTO agencyDTO = new AgencyDTO();
			agencyDTO.setAgency_code("agence1");
			agencyService.createAgency(agencyDTO);

			UserDTO userDTO = new UserDTO();
			userDTO.setUsername("Admin");
			userDTO.setPassword("123");
			userDTO.setEmail("admin@gmail.com");
			userDTO.setFirstName("test");
			userDTO.setLastName("test");
			int intUserId = 1;


// Convert int to Long
			Long userId = Long.valueOf(intUserId);

			userService.addAdmin(userDTO);


			System.out.println("Default admin user created: admin");

		};

	} */
	}





