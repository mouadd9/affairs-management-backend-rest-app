package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Utils.DateFormatterUtil;
import com.example.AffairsManagementApp.entities.Agency;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AgencyMapper {
    public AgencyDTO convertToDTO(@NotNull Agency agency){
        AgencyDTO agencyDTO = new AgencyDTO();
        agencyDTO.setId(agency.getId());
        agencyDTO.setAgencyCode(agency.getAgencyCode());
        agencyDTO.setAddress(agency.getAddress());
        agencyDTO.setStatus(agency.getStatus());
        agencyDTO.setFormattedCreationDate(DateFormatterUtil.formatDate(agency.getCreationDate()));
        return agencyDTO;
    }
    public Agency convertToEntity(@NotNull AgencyDTO agencyDTO){
        Agency agency = new Agency();
        agency.setAgencyCode(agencyDTO.getAgencyCode());
        agency.setAddress(agencyDTO.getAddress());
        agency.setStatus(agencyDTO.getStatus());
        return agency;
    }
}
