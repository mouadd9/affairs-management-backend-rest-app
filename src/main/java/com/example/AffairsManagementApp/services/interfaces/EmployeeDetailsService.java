package com.example.AffairsManagementApp.services.interfaces;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.Exceptions.UserIdNotFoundException;

public interface EmployeeDetailsService {
     AgencyDTO getAgencyByUserId(Long userId) throws UserIdNotFoundException;
}
