package com.example.AffairsManagementApp;
import com.example.AffairsManagementApp.Exceptions.UserAlreadyExistsException;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;
import com.example.AffairsManagementApp.mappers.RoleMapper;
import com.example.AffairsManagementApp.repositories.Userrepository;
import com.example.AffairsManagementApp.security.RsaKeyProperties;
import com.example.AffairsManagementApp.services.interfaces.AgencyService;
import com.example.AffairsManagementApp.services.interfaces.RoleService;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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


	}





