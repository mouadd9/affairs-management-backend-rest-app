package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.Exceptions.RoleAlreadyExistsException;
import com.example.AffairsManagementApp.Exceptions.RoleNotFoundException;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;
import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.entities.Role;
import com.example.AffairsManagementApp.mappers.RoleMapper;
import com.example.AffairsManagementApp.repositories.Rolerepository;
import com.example.AffairsManagementApp.repositories.Userrepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private Rolerepository rolerepository;
    private Userrepository userrepository;
    private RoleMapper roleMapper;

    @Override
    public RoleDTO addRole(String roleName) throws RoleAlreadyExistsException {
        // here we check if there is a role by the same name
        if (rolerepository.findByRoleName(roleName).isPresent()) {
            throw new RoleAlreadyExistsException("Role already exists");
        }
        // if not we create a new role
        Role role = new Role();
        // we set its name
        role.setRoleName(roleName);
        // then we save it and return its DTO
        return roleMapper.convertToDTO(rolerepository.save(role));
    }

    @Override
    public Role getRole(String roleName) throws RoleNotFoundException {
        return rolerepository.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("role with the name " + roleName + "not found"));
    }

    @Override
    public List<RoleDTO> getRolesByUserId(Long userId) throws UserIdNotFoundException {
        // here we extract the appUser by its ID
        AppUser appUser = userrepository.findById(userId).orElseThrow(()-> new UserIdNotFoundException("appUser with the id " + userId + "not found"));
        // then we convert each role to roleDTO while creating a knew list of roleDTOs
        List<RoleDTO> roleDTOS = appUser.getRoles().stream().map(role -> roleMapper.convertToDTO(role)).collect(Collectors.toList());
        return roleDTOS;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        // here we fetch for all roles
        List<Role> roles = rolerepository.findAll();
        // we convert them to DTOs
        List<RoleDTO> roleDTOS = roles.stream().map(role -> roleMapper.convertToDTO(role)).collect(Collectors.toList());
        return roleDTOS;
    }
}
