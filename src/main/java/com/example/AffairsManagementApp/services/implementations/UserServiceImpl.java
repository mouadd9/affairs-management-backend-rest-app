package com.example.AffairsManagementApp.services.implementations;

import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.Exceptions.*;
import com.example.AffairsManagementApp.entities.Agency;
import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.entities.EmployeeDetails;
import com.example.AffairsManagementApp.entities.Role;
import com.example.AffairsManagementApp.mappers.RoleMapper;
import com.example.AffairsManagementApp.mappers.UserMapper;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import com.example.AffairsManagementApp.repositories.EmployeeDetailsrepository;
import com.example.AffairsManagementApp.repositories.Rolerepository;
import com.example.AffairsManagementApp.repositories.Userrepository;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional // because our services interact with a db, this manages rolls back the entire method's operation if a constraint is violated
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final Agencyrepository agencyrepository;
    private final UserMapper userMapper;
    private final Rolerepository rolerepository;
    private final EmployeeDetailsrepository employeeDetailsrepository;
    private final Userrepository userrepository;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;


    // 1 add user
    @Override
    public UserDTO addUser(@NotNull UserDTO userDTO) throws UserAlreadyExistsException {
        // check if user exists by username
        // add user
        if(userrepository.findByUsername(userDTO.getUsername()).isPresent() || userrepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("appUser already exists");
        }
        AppUser appUser = userMapper.convertToEntity(userDTO);
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return userMapper.convertToDTO(userrepository.save(appUser));
    }

    // 2 add the role ADMIN to an existing AppUser
    @Override
    public void addAdminRole(Long userId) throws UserIdNotFoundException, RoleNotFoundException {
        // retrieve user -> check if exists
        // retrieve role -> check if exists
        // add role to user
        AppUser appUser = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("user not found"));
        Role existingRole = rolerepository.findByRoleName("ADMIN").orElseThrow(()->new RoleNotFoundException("role not found"));
        // here we will check if the role exists already using .contains to check if an element is in a list
        if (!appUser.getRoles().contains(existingRole)){
            appUser.getRoles().add(existingRole);
            userrepository.save(appUser);
        }

    }

    // 3 add the role AGENCY_EMPLOYEE to an existing AppUser
    @Override
    public void addAgencyEmployeeRole(Long userId, Long agencyId) throws RoleNotFoundException, UserIdNotFoundException, AgencyNotFoundException {

        // retrieve user -> check if exists
        // retrieve agency -> check if exists
        // retrieve role -> check if exists
        // add role to user

        AppUser appUser = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("user not found"));
        Agency agency = agencyrepository.findById(agencyId).orElseThrow(()->new AgencyNotFoundException("agency not found"));
        Role existingRole = rolerepository.findByRoleName("AGENCY_EMPLOYEE").orElseThrow(()->new RoleNotFoundException("role not found"));

        if (!appUser.getRoles().contains(existingRole)){
            appUser.getRoles().add(existingRole); // we add the role
            userrepository.save(appUser); // we save the user so no problems occur regarding existence checks
            EmployeeDetails employeeDetails = new EmployeeDetails();
            employeeDetails.setAppUser(appUser);
            employeeDetails.setAgency(agency);
            employeeDetailsrepository.save(employeeDetails);
        }
    }


    @Override
    public List<UserDTO> getAllUsers() {
        List<AppUser> appUsers = userrepository.findAll();
        // return appUsers.stream().map(user -> userMapper.convertToDTO(user)).toList();
        return appUsers.stream().map(userMapper::convertToDTO).toList();
    }

    @Override
    public AppUser getUserById(Long userId) throws UserIdNotFoundException {
        return userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("appUser with the id " + userId + "not found"));
    }

    @Override
    public UserDTO getUserDTOById(Long userId) throws UserIdNotFoundException {
        return null;
    }


    @Override
    public void deleteUser(Long userId) throws UserIdNotFoundException {
        // we Fetch the user to be deleted
        AppUser appUserToDelete = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("appUser with the id " + userId + "not found"));
        // Iterate through the user's roles
        //For each role, remove the user from the role's collection of users
        appUserToDelete.getRoles().forEach(role -> role.getAppUsers().remove(appUserToDelete));
        appUserToDelete.getRoles().clear();

        // If there's an associated EmployeeDetails, remove it
        if (appUserToDelete.getEmployeeDetails() != null) {
            employeeDetailsrepository.delete(appUserToDelete.getEmployeeDetails());
            appUserToDelete.setEmployeeDetails(null);
        }


        userrepository.delete(appUserToDelete);
    }



   /* @Override
    public List<RoleDTO> removeRoleFromUser(Long userId, String roleName) throws RoleNotFoundException, UserIdNotFoundException, RoleAlreadyRetrievedFromThisUser {
        return List.of();
    } */

   /* @Override
    public UserDTO addAgencyEmployee(UserDTO userDTO, Long agencyId) throws AgencyNotFoundException, RoleNotFoundException, UserAlreadyExistsException {
        // we check if the appUser already exists
        if (userrepository.findByUsername(userDTO.getUsername()).isPresent() || userrepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        // we check if the agency exists
        Agency agency = agencyrepository.findById(agencyId).orElseThrow(()-> new AgencyNotFoundException("agency with id" + agencyId + "not found"));
        // we extracted the role
        Role role = rolerepository.findByRoleName("AGENCY_EMPLOYEE")
                .orElseThrow(() -> new RoleNotFoundException("role with the name AGENCY_EMPLOYEE not found"));

        // conversion DTO --> Entity
        AppUser appUser = userMapper.convertToEntity(userDTO);
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        appUser.getRoles().add(role);
        AppUser savedAppUser = userrepository.save(appUser);
        // now that i have my role added i should add an entry to the Employee details
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setAppUser(savedAppUser);
        employeeDetails.setAgency(agency);
        employeeDetailsrepository.save(employeeDetails);

        return userMapper.convertToDTO(savedAppUser);
    }*/









}
