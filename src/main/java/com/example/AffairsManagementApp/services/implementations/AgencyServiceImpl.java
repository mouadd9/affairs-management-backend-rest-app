package com.example.AffairsManagementApp.services.implementations;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyCodeIsTakenException;
import com.example.AffairsManagementApp.Exceptions.AgencyHasEmployeesException;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Agency;
import com.example.AffairsManagementApp.enums.AgencyStatus;
import com.example.AffairsManagementApp.mappers.AgencyMapper;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import com.example.AffairsManagementApp.services.interfaces.AgencyService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AgencyServiceImpl implements AgencyService {
    Agencyrepository agencyRepository;
    AgencyMapper agencyMapper;
    @Override
    public AgencyDTO createAgency(@NotNull AgencyDTO agencyDTO) throws AgencyCodeIsTakenException {
        // we first check if the agencyCode already exists
        if ( agencyRepository.existsByAgencyCode(agencyDTO.getAgencyCode()) ){
            throw new AgencyCodeIsTakenException("this code is used by an agency");
        }
       Agency agency = agencyRepository.save(agencyMapper.convertToEntity(agencyDTO));

        return agencyMapper.convertToDTO(agency);
    }

    @Override
    public List<AgencyDTO> getAllAgencies() {

        List<Agency> agencies = agencyRepository.findAll();
        List<AgencyDTO> agencyDTOS = agencies.stream().map(agency -> agencyMapper.convertToDTO(agency)).collect(Collectors.toList());
        return agencyDTOS;
    }

    @Override
    public AgencyDTO getAgencyById(Long agencyId) throws AgencyNotFoundException {
        Agency agency = agencyRepository.findById(agencyId).orElseThrow(()-> new AgencyNotFoundException("agency with id" + agencyId + "not found"));
        return agencyMapper.convertToDTO(agency);
    }

    @Override
    public AgencyDTO updateAgency(Long agencyId, AgencyDTO agencyDTO) throws AgencyNotFoundException, AgencyCodeIsTakenException {

        // here we check if the agency exists
        Agency existingAgency = agencyRepository.findById(agencyId).orElseThrow(()-> new AgencyNotFoundException("agency with id" + agencyId + "not found"));

        // here we check if the agency code is taken
        if (!existingAgency.getAgencyCode().equals(agencyDTO.getAgencyCode()) &&
                agencyRepository.existsByAgencyCode(agencyDTO.getAgencyCode())) {
            throw new AgencyCodeIsTakenException("This code is used by another agency");
        }
        /*If the code is the same as the current one,it remains unchanged.
         If it’s different and unique, it gets updated.*/
        existingAgency.setAgencyCode(agencyDTO.getAgencyCode());
        existingAgency.setAddress(agencyDTO.getAddress());
        existingAgency.setStatus(agencyDTO.getStatus());
        Agency saved = agencyRepository.save(existingAgency);
        return agencyMapper.convertToDTO(saved);
    }

    @Override
    public void deleteAgency(Long agencyId) throws AgencyNotFoundException, AgencyHasEmployeesException {
        Agency agencyToRemove = agencyRepository.findById(agencyId).orElseThrow(()-> new AgencyNotFoundException("agency with id" + agencyId + "not found"));
        if (!agencyToRemove.getEmployeeDetailsList().isEmpty()){
            throw new AgencyHasEmployeesException("agency can not be deleted it has employees");
        }
        agencyRepository.delete(agencyToRemove);
    }

    @Override
    public Map<String, Long> getAgencyCount() {
        Map<String, Long> countsByAgency = new HashMap<>();
        List<Agency> allAgencies = agencyRepository.findAll();
        long activeAgencies = allAgencies.stream().filter(agency -> agency.getStatus().equals(AgencyStatus.ACTIVE)).count();

        countsByAgency.put("active", activeAgencies);
        countsByAgency.put("inactive", (long) allAgencies.size() - activeAgencies);
        countsByAgency.put("total", (long) allAgencies.size());
        // here we should iterate over agencies

        return countsByAgency;
    }

    @Override
    public void updateStatus(Long agencyId, AgencyStatus newStatus) throws AgencyNotFoundException {
        Agency agencyToUpdate = agencyRepository.findById(agencyId).orElseThrow(()-> new AgencyNotFoundException("agency with id" + agencyId + "not found"));

        agencyToUpdate.setStatus(newStatus);
        agencyRepository.save(agencyToUpdate);
    }


}
