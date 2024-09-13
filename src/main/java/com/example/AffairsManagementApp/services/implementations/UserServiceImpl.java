package com.example.AffairsManagementApp.services.implementations;

import com.example.AffairsManagementApp.DTOs.AgencyEmployeeDTO;
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
import com.example.AffairsManagementApp.services.interfaces.RoleService;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final RoleService roleService;
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

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) throws UserAlreadyExistsException, UserIdNotFoundException {
        AppUser existingUser = userrepository.findById(userId)
                .orElseThrow(() -> new UserIdNotFoundException("User not found with id: " + userId));
        // here we search for a user with the same username as the one we are trying to use
        AppUser userWithSameUsername = userrepository.findByUsername(userDTO.getUsername()).orElse(null);
        // if it exists , and its with diff id than us , than we should throw an error meaning this username is already used by another user
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(userId)) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        // Check if email is taken by another user
        AppUser userWithSameEmail = userrepository.findByEmail(userDTO.getEmail()).orElse(null);
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(userId)) {
            throw new UserAlreadyExistsException("Email already taken");
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        AppUser updatedUser = userrepository.save(existingUser);
        return userMapper.convertToDTO(updatedUser);
    }

    @Override
    public AgencyEmployeeDTO getEmployeeDetailsByUsername(String username) throws UsernameNotFoundException, EntityNotFoundException {
        AppUser user = this.userrepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " is not found"));
        EmployeeDetails employeeDetails = this.employeeDetailsrepository.findByAppUser(user).orElseThrow(() -> new EntityNotFoundException("Employee details not found for user " + username));
        AgencyEmployeeDTO agencyEmployeeDTO = new AgencyEmployeeDTO();
        agencyEmployeeDTO.setId(employeeDetails.getAppUser().getId());
        agencyEmployeeDTO.setUsername(employeeDetails.getAppUser().getUsername());
        agencyEmployeeDTO.setEmail(employeeDetails.getAppUser().getEmail());
        agencyEmployeeDTO.setLastName(employeeDetails.getAppUser().getLastName());
        agencyEmployeeDTO.setFirstName(employeeDetails.getAppUser().getFirstName());
        agencyEmployeeDTO.setAgencyId(employeeDetails.getAgency().getId());
        agencyEmployeeDTO.setAgencyCode(employeeDetails.getAgency().getAgencyCode());
        return agencyEmployeeDTO;
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

    @Override
    public List<UserDTO> getUsersByAgency(Long agencyId) throws AgencyNotFoundException {
        Agency agency = agencyrepository.findById(agencyId).orElseThrow(()->new AgencyNotFoundException("agency not found"));
        List<UserDTO> users = agency.getEmployeeDetailsList().stream().map(employeeDetails -> userMapper.convertToDTO(employeeDetails.getAppUser())).toList();
        return users;
    }

    @Override
    public Map<String, Long> getUserCounts() throws RoleNotFoundException{
        Map<String, Long> countsByRole = new HashMap<>();

        // so we will get all roles
        // for each role we will see the number of users that it has
        List<RoleDTO> roles = roleService.getAllRoles();

        for (RoleDTO roleDTO : roles) {
            Long count = roleService.getUsersCountByRole(roleDTO.getRoleName());
            countsByRole.put(roleDTO.getRoleName(), count != null ? count : 0L);
        }

        countsByRole.put("sum", userrepository.count());

        return countsByRole;


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
