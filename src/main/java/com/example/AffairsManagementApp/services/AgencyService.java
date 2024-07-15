package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Agence;

import java.util.List;

public interface AgencyService {
    AgencyDTO createAgency(AgencyDTO agencyDTO);
    List<AgencyDTO> getAllAgencies();
    AgencyDTO getAgencyById(Long agenceId) throws AgencyNotFoundException;
}
