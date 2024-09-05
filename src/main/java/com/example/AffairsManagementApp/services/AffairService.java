package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AffairDTO;
import com.example.AffairsManagementApp.Exceptions.AffairNotFoundException;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Affair;

import java.util.List;
import java.util.Optional;

public interface AffairService {
    AffairDTO createAffair(AffairDTO affair) throws AgencyNotFoundException; // the affair will be sent from the user
    Long getCount();
    List<AffairDTO> getAffairs();
    Long getCountByAgency(Long id) throws AgencyNotFoundException;
    List<AffairDTO> getAffairsByAgency(Long id) throws AgencyNotFoundException;

    AffairDTO updateAffair(AffairDTO affairDTO) throws AffairNotFoundException;

    void deleteAffair(Long id) throws AffairNotFoundException;
}

