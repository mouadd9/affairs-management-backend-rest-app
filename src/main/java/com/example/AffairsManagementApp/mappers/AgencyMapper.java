package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.entities.Agency;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AgencyMapper {
    public AgencyDTO convertToDTO(@NotNull Agency agency){
        AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(agency.getId());
        agencyDTO.setAgency_code(agency.getAgency_code());
        return agencyDTO;
    }
    public Agency convertToEntity(@NotNull AgencyDTO agencyDTO){
        Agency agency = new Agency();
        agency.setAgency_code(agencyDTO.getAgency_code());
        return agency;
    }
}
