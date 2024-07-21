package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;
import com.example.AffairsManagementApp.entities.Agency;
import com.example.AffairsManagementApp.mappers.AgencyMapper;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AgencyServiceImpl implements AgencyService{
    Agencyrepository agencyRepository;
    AgencyMapper agencyMapper;
    @Override
    public AgencyDTO createAgency(AgencyDTO agencyDTO) {
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

}
