package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.entities.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserMapper {


    public UserDTO convertToDTO(@NotNull AppUser appUser){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(appUser.getFirstName());
        userDTO.setLastName(appUser.getLastName());
        userDTO.setId(appUser.getId());
        userDTO.setEmail(appUser.getEmail());
        userDTO.setUsername(appUser.getUsername());
        // Convert roles to a collection of role names
        userDTO.setRoles(appUser.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList()));
        return userDTO;
    }
    public AppUser convertToEntity(@NotNull UserDTO userDTO) {
        AppUser appUser = new AppUser();
        appUser.setFirstTimeAuth(true); // at initial creation this will be set to true
        appUser.setUsername(userDTO.getUsername());
        appUser.setEmail(userDTO.getEmail());
        appUser.setFirstName(userDTO.getFirstName());
        appUser.setLastName(userDTO.getLastName());
        appUser.setPassword(userDTO.getPassword());
        return appUser;
    }

}
