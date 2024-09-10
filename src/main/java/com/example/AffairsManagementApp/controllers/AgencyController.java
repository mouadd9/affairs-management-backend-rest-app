package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyCodeIsTakenException;
import com.example.AffairsManagementApp.Exceptions.AgencyHasAffairsException;
import com.example.AffairsManagementApp.Exceptions.AgencyHasEmployeesException;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.enums.AgencyStatus;
import com.example.AffairsManagementApp.services.interfaces.AgencyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agencies")
public class AgencyController {

    private final AgencyService agencyService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AgencyDTO> createAgency(@Valid @RequestBody AgencyDTO agency) {
        try {
            AgencyDTO newAgency = agencyService.createAgency(agency);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAgency);
        } catch(AgencyCodeIsTakenException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }


    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> getAllAgencies(@RequestParam(defaultValue = "false") Boolean countOnly) {
        if (countOnly) {
            // if we only need the count of agencies
            Map<String, Long> count = agencyService.getAgencyCount();

            return ResponseEntity.status(HttpStatus.OK).body(count);

        } else {

            List<AgencyDTO> agencies = agencyService.getAllAgencies();
            return ResponseEntity.ok(agencies);

        }


    } // this will be used to get all agencies and put them in a table

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable(name = "id") Long id) {
        try {
            AgencyDTO agencyDTO = agencyService.getAgencyById(id);
            return ResponseEntity.ok(agencyDTO);
        } catch (AgencyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AgencyDTO> updateAgency(@PathVariable(name = "id") Long id,@Valid @RequestBody AgencyDTO agencyDTO){
        try {
            AgencyDTO agency = agencyService.updateAgency(id, agencyDTO);
            return ResponseEntity.ok(agency); // return ResponseEntity.status(HttpStatus.OK).body(agency);
            // here we use a service for updating an agency's info
        } catch (AgencyNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AgencyCodeIsTakenException e){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        }
    }

    //this endpoint will get the id of the agency to patch and the new status
    @PatchMapping("/{id}/status/{newState}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> updateStatus(@PathVariable(name = "id") Long agencyId,@PathVariable(name = "newState") AgencyStatus newStatus){

        try {

            agencyService.updateStatus(agencyId, newStatus);
            return ResponseEntity.status(HttpStatus.OK).body(null);

            // here we will send these two arguments to a service that fetch for the agency and chenge its status
            // we will catch an exception if the agency doesnt exist
        } catch(AgencyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteAgency(@PathVariable(name = "id") Long agencyId){
        try {
            agencyService.deleteAgency(agencyId);
            // return ResponseEntity.noContent().build();
            // return new ResponseEntity<>(null,HttpStatus.NO_CONTENT );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch(AgencyHasEmployeesException | AgencyHasAffairsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch(AgencyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
