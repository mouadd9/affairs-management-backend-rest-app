package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;

public interface EmployeeDetailsService {
     AgencyDTO getAgencyByUserId(Long userId) throws UserIdNotFoundException;
}
