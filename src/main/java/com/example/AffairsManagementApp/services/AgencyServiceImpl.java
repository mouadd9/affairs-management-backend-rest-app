package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Agence;
import com.example.AffairsManagementApp.mappers.AgencyMapper;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import lombok.AllArgsConstructor;
import lombok.Data;
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
       Agence agence = agencyRepository.save(agencyMapper.convertToEntity(agencyDTO));

        return agencyMapper.convertToDTO(agence);
    }

    @Override
    public List<AgencyDTO> getAllAgencies() {

        List<Agence> agences = agencyRepository.findAll();
        List<AgencyDTO> agencyDTOS = agences.stream().map(agence -> agencyMapper.convertToDTO(agence)).collect(Collectors.toList());
        return agencyDTOS;
    }

    @Override
    public AgencyDTO getAgencyById(Long agencyId) throws AgencyNotFoundException {
        Agence agence = agencyRepository.findById(agencyId).orElseThrow(()-> new AgencyNotFoundException("agency with id" + agencyId + "not found"));
        return agencyMapper.convertToDTO(agence);
    }
}
