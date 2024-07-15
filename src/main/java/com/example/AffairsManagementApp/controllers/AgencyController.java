package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Agence;
import com.example.AffairsManagementApp.services.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/agency")
public class AgencyController {
    @Autowired
    private AgencyService agencyService;

    // Endpoint to create a new agency
    @PostMapping("/")
    public ResponseEntity<AgencyDTO> createAgency(@RequestBody AgencyDTO agency) {
        AgencyDTO newAgency = agencyService.createAgency(agency);
        return ResponseEntity.ok(newAgency);
    }

    // Endpoint to retrieve all agencies
    @GetMapping("/agencies")
    public ResponseEntity<List<AgencyDTO>> getAllAgencies() {

        List<AgencyDTO> agencies = agencyService.getAllAgencies();
        return ResponseEntity.ok(agencies);
    }

    // Endpoint to retrieve a specific agency by ID
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
