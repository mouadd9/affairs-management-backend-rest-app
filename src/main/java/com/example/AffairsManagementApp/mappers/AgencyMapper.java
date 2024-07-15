package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.entities.Agence;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AgencyMapper {
    public AgencyDTO convertToDTO(@NotNull Agence agence){
        AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(agence.getId());
        agencyDTO.setName(agence.getName());
        return agencyDTO;
    }
    public Agence convertToEntity(@NotNull AgencyDTO agencyDTO){
        Agence agency = new Agence();
        agency.setName(agencyDTO.getName());
        return agency;
    }
}
