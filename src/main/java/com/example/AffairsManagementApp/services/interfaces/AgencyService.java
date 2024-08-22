package com.example.AffairsManagementApp.services.interfaces;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyCodeIsTakenException;
import com.example.AffairsManagementApp.Exceptions.AgencyHasEmployeesException;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.enums.AgencyStatus;

import java.util.List;
import java.util.Map;

public interface AgencyService {
    AgencyDTO createAgency(AgencyDTO agencyDTO) throws AgencyCodeIsTakenException;
    List<AgencyDTO> getAllAgencies();
    AgencyDTO getAgencyById(Long agencyId) throws AgencyNotFoundException;
    AgencyDTO updateAgency(Long agencyId, AgencyDTO agencyDTO) throws AgencyNotFoundException, AgencyCodeIsTakenException;
    void deleteAgency(Long agencyId) throws AgencyNotFoundException, AgencyHasEmployeesException;

    Map<String, Long> getAgencyCount();

    void updateStatus(Long agencyId, AgencyStatus newStatus) throws AgencyNotFoundException;
}
