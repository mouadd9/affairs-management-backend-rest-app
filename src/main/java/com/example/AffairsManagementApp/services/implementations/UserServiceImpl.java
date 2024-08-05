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

    @Override
    public List<RoleDTO> addAgencyEmployeeRoleToUser(Long userId, Long agencyId) throws RoleNotFoundException, UserIdNotFoundException,RoleAlreadyAssignedToThisUser, AgencyNotFoundException {
        // first we check if the role, AppUser and agency exist if so we store them
        AppUser appuser = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("user not found"));
        Agency agency = agencyrepository.findById(agencyId).orElseThrow(()->new AgencyNotFoundException("agency not found"));
        Role existingRole = rolerepository.findByRoleName("AGENCY_EMPLOYEE").orElseThrow(()->new RoleNotFoundException("role not found"));
        // if then we check if the user has the AGENCY_EMPLOYEE role
            // we store the array
        List<Role> roles = appuser.getRoles().stream().toList();
            // we check if the role does exist in role array belonging to our app user
        if(roles.stream().anyMatch(role-> existingRole.getRoleName().equals(role.getRoleName()))){
             throw new RoleAlreadyAssignedToThisUser("role already assigned");
        }
        // if we passed these checks then :
        // our app user does exist
        // our agency does exist
        // the role we want to add does exist
        // the user does not have the role yet

        // so now we must add the role to our user
        appuser.getRoles().add(existingRole);
        // then we save our user
        AppUser savedUser = userrepository.save(appuser);
        // then we must add an entry in our
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setAppUser(appuser);
        employeeDetails.setAgency(agency);
        employeeDetailsrepository.save(employeeDetails);
        List<RoleDTO> roleDTOS= appuser.getRoles().stream().map(role -> roleMapper.convertToDTO(role)).collect(Collectors.toList());
        // return all roles
        return roleDTOS;
    }

   /* @Override
    public List<RoleDTO> removeRoleFromUser(Long userId, String roleName) throws RoleNotFoundException, UserIdNotFoundException, RoleAlreadyRetrievedFromThisUser {
        return List.of();
    } */

    @Override
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
    }

    @Override
    public UserDTO addUser(@NotNull UserDTO userDTO) throws UserAlreadyExistsException {
        // first we check if the appUser is already registered
        if(userrepository.findByUsername(userDTO.getUsername()).isPresent() || userrepository.findByEmail(userDTO.getEmail()).isPresent()){
             throw new UserAlreadyExistsException("appUser already exists");
        }
        AppUser appUser = userMapper.convertToEntity(userDTO);
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return userMapper.convertToDTO(userrepository.save(appUser));
    }

    @Override
    public UserDTO addAdmin(UserDTO userDTO) throws UserAlreadyExistsException, RoleNotFoundException {
        // first we check if the appUser is already registered
        if(userrepository.findByUsername(userDTO.getUsername()).isPresent() || userrepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("appUser already exists");
        }
        AppUser appUser = userMapper.convertToEntity(userDTO);
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        Role role = rolerepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new RoleNotFoundException("role with the name AGENCY_EMPLOYEE not found"));

        appUser.getRoles().add(role);
        return userMapper.convertToDTO(userrepository.save(appUser));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<AppUser> appUsers = userrepository.findAll();
        List<UserDTO> userDTOS = appUsers.stream().map(user -> userMapper.convertToDTO(user)).toList();
        return userDTOS;
    }

    @Override
    public AppUser getUserById(Long userId) throws UserIdNotFoundException {
        AppUser appUser = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("appUser with the id " + userId + "not found"));
        return appUser;
    }

    @Override
    public UserDTO getUserDTOById(Long userId) throws UserIdNotFoundException {
        return null;
    }

    @Override
    public void deleteUser(Long userId) throws UserIdNotFoundException {
        AppUser appUserToDelete = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("appUser with the id " + userId + "not found"));
        userrepository.delete(appUserToDelete);
    }

}
