package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.entities.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleMapper {
    public RoleDTO convertToDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setId(role.getId());
        return roleDTO;

    }
    public Role convertToEntity(RoleDTO roleDTO){
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        role.setId(roleDTO.getId());
        return role;
    }

}
