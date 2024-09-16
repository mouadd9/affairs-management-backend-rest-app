package com.example.AffairsManagementApp.DTOs;

import com.example.AffairsManagementApp.entities.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private Boolean firstTimeAuth;
    private String username;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String firstName;
    private String lastName;

    private Collection<String> roles; // Added roles as a collection of role names, we only cherry-pick the role names



}
