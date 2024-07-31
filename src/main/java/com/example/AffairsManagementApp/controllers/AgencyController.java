package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.services.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/agency")
public class AgencyController {
    @Autowired
    private AgencyService agencyService;

    // create a new agency
    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<AgencyDTO> createAgency(@RequestBody AgencyDTO agency) {
        AgencyDTO newAgency = agencyService.createAgency(agency);
        return ResponseEntity.ok(newAgency);
    }

    // retrieve all agencies
    @GetMapping("/agencies")
    public ResponseEntity<List<AgencyDTO>> getAllAgencies() {

        List<AgencyDTO> agencies = agencyService.getAllAgencies();
        return ResponseEntity.ok(agencies);
    }

    // retrieve a specific agency by ID
    @GetMapping("/{id}")
    public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable(name = "id") Long id) {
        try {
            AgencyDTO agencyDTO = agencyService.getAgencyById(id);
            return ResponseEntity.ok(agencyDTO);
        } catch (AgencyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
