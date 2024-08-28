package com.example.AffairsManagementApp.services.interfaces;

import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.Exceptions.*;
import com.example.AffairsManagementApp.entities.AppUser;

import java.util.List;
import java.util.Map;

public interface UserService {
    // here we will put the methods a site user would need
    // first we have roles
    // so after an admin authenticates , he will be able to manipulate users
    // the dashboard will have these segments
    // users this will list all users (getallusers)
    // a segment called add user
    // it will have the options (add agency employee --> u gotta choose the agency/ add backoffice /add admin)

    // we will pass the userDTO and agencyID
    // we will check if the agency exists
    // we will convert userDTO to user
    // we will add Role if exists to the role array in the user
    // we will add an entry using the user id and the agency id
    // then save


    // List<RoleDTO> removeRoleFromUser(Long userId, String roleName) throws RoleNotFoundException, UserIdNotFoundException, RoleAlreadyRetrievedFromThisUser;
    // UserDTO addAdmin(UserDTO userDTO) throws UserAlreadyExistsException, RoleNotFoundException;
    // UserDTO addAgencyEmployee(UserDTO userDTO, Long agencyId) throws AgencyNotFoundException, RoleNotFoundException, UserAlreadyExistsException;


    UserDTO addUser(UserDTO userDTO) throws UserAlreadyExistsException;
    void addAgencyEmployeeRole(Long userId, Long agencyId) throws RoleNotFoundException, UserIdNotFoundException , AgencyNotFoundException;
    void addAdminRole(Long userId) throws UserIdNotFoundException, RoleNotFoundException;
    // retrieving users
    List<UserDTO> getAllUsers() ;
    AppUser getUserById(Long userId) throws UserIdNotFoundException;
    UserDTO getUserDTOById(Long userId) throws UserIdNotFoundException;
    // deletion
    void deleteUser(Long userId) throws UserIdNotFoundException;
    List<UserDTO> getUsersByAgency(Long agencyId) throws AgencyNotFoundException;
    Map<String, Long> getUserCounts() throws RoleNotFoundException;

    UserDTO updateUser(Long userId, UserDTO userDTO) throws UserAlreadyExistsException, UserIdNotFoundException;
}
