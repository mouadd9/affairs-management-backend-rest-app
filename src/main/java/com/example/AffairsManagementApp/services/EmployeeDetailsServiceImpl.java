package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;
import com.example.AffairsManagementApp.entities.Agency;
import com.example.AffairsManagementApp.entities.EmployeeDetails;
import com.example.AffairsManagementApp.mappers.AgencyMapper;
import com.example.AffairsManagementApp.repositories.EmployeeDetailsrepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {
    private EmployeeDetailsrepository employeeDetailsrepository;
    private AgencyMapper agencyMapper;
    @Override
    public AgencyDTO getAgencyByUserId(Long userId) throws UserIdNotFoundException {
        EmployeeDetails employeeDetails = employeeDetailsrepository.findByAppUserId(userId)
                .orElseThrow(()-> new UserIdNotFoundException("user with the id " + userId + "not found"));
        Agency agency = employeeDetails.getAgency();
        return agencyMapper.convertToDTO(agency);

    }
}
