package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.EmployeeDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Agence;
import com.example.AffairsManagementApp.entities.Employe;
import com.example.AffairsManagementApp.enums.RoleType;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import com.example.AffairsManagementApp.services.AgencyService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {


    public EmployeeDTO convertToDTO(@NotNull Employe employe){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(employe.getFirstName());
        employeeDTO.setLastName(employe.getLastName());
        employeeDTO.setId(employe.getId());
        employeeDTO.setEmail(employe.getEmail());
        employeeDTO.setUsername(employe.getUsername());
        employeeDTO.setPassword(employe.getPassword());
        return employeeDTO;
    }
    public Employe convertToEntity(@NotNull EmployeeDTO employeeDTO) {
        Employe employe = new Employe();
        employe.setUsername(employeeDTO.getUsername());
        employe.setEmail(employeeDTO.getEmail());
        employe.setFirstName(employeeDTO.getFirstName());
        employe.setLastName(employeeDTO.getLastName());
        employe.setPassword(employeeDTO.getPassword());
        return employe;
    }

}
