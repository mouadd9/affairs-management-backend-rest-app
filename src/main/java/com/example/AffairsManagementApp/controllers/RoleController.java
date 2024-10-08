package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.Exceptions.RoleAlreadyExistsException;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;
import com.example.AffairsManagementApp.services.interfaces.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/{roleName}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<RoleDTO> addNewRole(@PathVariable(name = "roleName") String roleName){
        try {
            RoleDTO roleDTO = roleService.addRole(roleName);
            return new ResponseEntity<>(roleDTO , HttpStatus.CREATED);
        } catch (RoleAlreadyExistsException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        // ResponseEntity is a generic class, which means we can deliberately choose the type of the body
        // it gives us a certain freedom, because the Body of ResponseEntity can be in various types
    }

   // get all roles
    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<RoleDTO>> getAllRoles(){
        List<RoleDTO> roleDTOS = roleService.getAllRoles();
        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }

    // get roles by user
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<RoleDTO>> getRolesByUserId(@PathVariable Long userId){
        try {
            List<RoleDTO> roleDTOS = roleService.getRolesByUserId(userId);
            return new ResponseEntity<>(roleDTOS, HttpStatus.OK); // here we put the body and the status
        } catch(UserIdNotFoundException e){ // this will treat the case when the user does not exist
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



}
