package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.DTOs.RoleDTO;
import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.Exceptions.*;
import com.example.AffairsManagementApp.services.interfaces.EmployeeDetailsService;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeDetailsService employeeDetailsService;


   // create an agency employee and assigns Agency role automatically.
   @PostMapping("/agency_employee/{agencyId}")
   @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
   public ResponseEntity<UserDTO> addEmployee(@RequestBody UserDTO userDTO, @PathVariable (name = "agencyId") Long agencyId) {
        try {
            UserDTO savedEmployee = userService.addAgencyEmployee(userDTO,agencyId);
            return new ResponseEntity<>(savedEmployee, HttpStatus.OK);
        } catch (AgencyNotFoundException | RoleNotFoundException e) {
            // if the agency is not found then we will send 400 status
            return ResponseEntity.notFound().build();
        } catch (UserAlreadyExistsException e){
            // if the user already exists
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }


   // Adds a user without any roles.
    @PostMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO){
       try {
           // now we will call the userService to add a user to the user table without setting a role
           UserDTO savedUser = userService.addUser(userDTO);
           return new ResponseEntity<>(savedUser,HttpStatus.OK );
       } catch (UserAlreadyExistsException e){
           // if the user already exists
           return new ResponseEntity<>(null, HttpStatus.CONFLICT);
       }

    }

    // Adds a Admin without any roles.
    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDTO> addAdmin(@RequestBody UserDTO userDTO){
        try {
            // now we will call the userService to add a user to the user table without setting a role
            UserDTO savedUser = userService.addAdmin(userDTO);
            return new ResponseEntity<>(savedUser,HttpStatus.OK );
        } catch (UserAlreadyExistsException e){
            // if the user already exists
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (RoleNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // list all users

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
       List<UserDTO> allUsers = userService.getAllUsers();
       return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    // get an employee's agency
    @GetMapping("/agency/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AgencyDTO> getAgencyByEmployee(@PathVariable Long userId){
       try {
           AgencyDTO agencyDTO = employeeDetailsService.getAgencyByUserId(userId);
           return new ResponseEntity<>(agencyDTO, HttpStatus.OK);

       } catch( UserIdNotFoundException e){
           return ResponseEntity.notFound().build();
       }
    }

    // list agency employees

    // assign agency employee role to a user with no role or with (admin/backoffice) role
    @PostMapping("/{userId}/agencies/{agencyId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<RoleDTO>> addAgencyEmployeeRoleToUser(@PathVariable Long userId, @PathVariable Long agencyId){
       try {
           List<RoleDTO> roleDTOS = userService.addAgencyEmployeeRoleToUser(userId, agencyId);
           return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
       } catch(RoleNotFoundException | UserIdNotFoundException | AgencyNotFoundException e) {
           return ResponseEntity.notFound().build();
       } catch(RoleAlreadyAssignedToThisUser e){
           return new ResponseEntity<>(null, HttpStatus.CONFLICT);
       }

    }
    // delete agency employee role to a user that has employee agency role


}
