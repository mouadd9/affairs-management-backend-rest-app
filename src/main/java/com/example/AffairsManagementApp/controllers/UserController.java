package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.Exceptions.*;
import com.example.AffairsManagementApp.services.interfaces.EmployeeDetailsService;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmployeeDetailsService employeeDetailsService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        try {
            // now we will call the userService to add a user to the user table without setting a role
            UserDTO savedUser = userService.addUser(userDTO);
            return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e){
            // if the user already exists
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> addAdminRole(@PathVariable(name = "userId" ) Long userId){
        try {
            userService.addAdminRole(userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UserIdNotFoundException | RoleNotFoundException e ){
            // if the user already exists
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PutMapping("/{userId}/agencies/{agencyId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> addAgencyEmployeeRole(@PathVariable Long userId, @PathVariable Long agencyId){
        try {
            userService.addAgencyEmployeeRole(userId, agencyId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(RoleNotFoundException | UserIdNotFoundException | AgencyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "false") Boolean countOnly){

        if(countOnly){

            try {


                Map<String, Long> userCountsByRole = userService.getUserCounts(); // here we will return an object of type :
                return ResponseEntity.status(HttpStatus.OK).body(userCountsByRole);
            } catch (RoleNotFoundException e) {

                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // if there is a user with no roles

            }

        } else {

            List<UserDTO> allUsers = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(allUsers);
        }

    }

    // get an employee's agency
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AgencyDTO> getAgencyByEmployee(@PathVariable Long userId){
        try {
            AgencyDTO agencyDTO = employeeDetailsService.getAgencyByUserId(userId);
            return new ResponseEntity<>(agencyDTO, HttpStatus.OK);

        } catch( UserIdNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (UserIdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

  /* // create an agency employee and assigns Agency role automatically.
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
    } */
}
