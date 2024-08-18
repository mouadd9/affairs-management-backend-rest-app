package com.example.AffairsManagementApp.services.interfaces;

import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.Exceptions.RoleAlreadyExistsException;
import com.example.AffairsManagementApp.Exceptions.RoleNotFoundException;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;
import com.example.AffairsManagementApp.entities.Role;

import java.util.List;

public interface RoleService {
    RoleDTO addRole(String roleName) throws RoleAlreadyExistsException;
    Role getRole(String roleName) throws RoleNotFoundException;
    List<RoleDTO> getRolesByUserId(Long userId) throws UserIdNotFoundException;
    List<RoleDTO> getAllRoles();
    Long getUsersCountByRole(String roleName) throws RoleNotFoundException;
}
