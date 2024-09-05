package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.AffairDTO;
import com.example.AffairsManagementApp.Exceptions.AffairNotFoundException;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.services.AffairService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/affairs")
public class AffairController {

    private final AffairService affairService;

    // here we will add the following endpoints
    // post /api/affairs (body)
    // get /api/affairs true: count , false: affairs / get(by agency id) /api/affairs/{id} true: count , false: affairs
    // delete /api/affairs/{id}
    // put /api/affairs/{id}

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<AffairDTO> createAffair(@RequestBody AffairDTO affairDTO){

        try{

            AffairDTO affair = this.affairService.createAffair(affairDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(affair);

        } catch (AgencyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<AffairDTO> updateAffair(@RequestBody AffairDTO affairDTO){
        try {
            AffairDTO affair = this.affairService.updateAffair(affairDTO);
            return ResponseEntity.status(HttpStatus.OK).body(affair);

        } catch (AffairNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<?> getAffairs(@RequestParam(defaultValue = "false") Boolean countOnly) {
        // if no param is passed we set it to false
        if(countOnly){
            Long count = this.affairService.getCount();
            return ResponseEntity.status(HttpStatus.OK).body(count);
        } else {
            List<AffairDTO> affairs = this.affairService.getAffairs();
            return ResponseEntity.status(HttpStatus.OK).body(affairs);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<?> getAffairsByAgency(@RequestParam(defaultValue = "false") Boolean countOnly, @PathVariable(name = "id") Long id ) {
        // if no param is passed we set it to false
        try {

            if(countOnly){
                Long count = this.affairService.getCountByAgency(id);
                return ResponseEntity.status(HttpStatus.OK).body(count);
            } else {
                List<AffairDTO> affairs = this.affairService.getAffairsByAgency(id);
                return ResponseEntity.status(HttpStatus.OK).body(affairs);
            }

        } catch (AgencyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_AGENCY_EMPLOYEE')")
    public ResponseEntity<Void> deleteAffair(@PathVariable Long id){
        try {
            this.affairService.deleteAffair(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (AffairNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }





}
