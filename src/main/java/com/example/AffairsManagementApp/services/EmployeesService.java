package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.EmployeeDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.Exceptions.EmployeeNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EmployeesService {

    EmployeeDTO saveEmployeeByAgency(@NotNull EmployeeDTO employeeDTO, Long agencyId) throws AgencyNotFoundException;

    void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    List<EmployeeDTO> getEmployeesByAgency(Long agencyId) throws AgencyNotFoundException ;
}
