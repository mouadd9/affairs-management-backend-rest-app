package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.DTOs.AgencyEmployeeDTO;
import com.example.AffairsManagementApp.DTOs.UserDTO;
import com.example.AffairsManagementApp.Exceptions.*;
import com.example.AffairsManagementApp.services.interfaces.EmployeeDetailsService;
import com.example.AffairsManagementApp.services.interfaces.UserService;
import lombok.AllArgsConstructor;
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

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam("username") String username, @RequestParam("newPassword") String newPassword){
        try{
            userService.resetPassword(username, newPassword);
            return ResponseEntity.ok().build();
        } catch (UsernameNotFoundException e){
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping("/first-login-status/{username}")
    public ResponseEntity<Boolean> checkFirstLoginStatus(@PathVariable String username) {
        try {
            Boolean status = userService.checkFirstLoginStatus(username);
            return ResponseEntity.ok(status);
        } catch (UsernameNotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }
    @PutMapping("/{userId}/add-admin-role")
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

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO){

        try {
            UserDTO user = userService.updateUser(userId, userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (UserAlreadyExistsException e){
            String errorMessage = null;

            if (e.getMessage().contains("Username")){
                errorMessage = "User with this username already exists";
            }  else if (e.getMessage().contains("Email")) {
                errorMessage = "User with this email already exists";
            }
                // if the user already exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
            } catch (UserIdNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
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

    @GetMapping("/agencies/{agencyId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByAgency(@PathVariable Long agencyId){

        try {

            List<UserDTO> userDTOS = userService.getUsersByAgency(agencyId);
            return ResponseEntity.status(HttpStatus.OK).body(userDTOS);
        }catch(AgencyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

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

    @GetMapping("/details/{username}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<AgencyEmployeeDTO> getEmployeeDetails(@PathVariable String username){
        try {
            AgencyEmployeeDTO agencyEmployeeDTO = this.userService.getEmployeeDetailsByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(agencyEmployeeDTO);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
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
