package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.DTOs.AgencyDTO;
import com.example.AffairsManagementApp.DTOs.EmployeeDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.Exceptions.EmployeeNotFoundException;
import com.example.AffairsManagementApp.entities.Agence;
import com.example.AffairsManagementApp.entities.Employe;
import com.example.AffairsManagementApp.enums.RoleType;
import com.example.AffairsManagementApp.mappers.AgencyMapper;
import com.example.AffairsManagementApp.mappers.EmployeeMapper;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import com.example.AffairsManagementApp.repositories.Employerepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class EmployeesServiceImpl implements EmployeesService{
    private final Employerepository employeRepository;
    private final AgencyService agencyService;
    private final AgencyMapper agencyMapper;
    private final EmployeeMapper employeeMapper;
    private final Agencyrepository agencyrepository;
    @Override
    public EmployeeDTO saveEmployeeByAgency(@NotNull EmployeeDTO employeeDTO, Long agencyId) throws AgencyNotFoundException {

        // first we check if the agency exists
        Agence agence = agencyrepository.findById(agencyId).orElseThrow(() -> new AgencyNotFoundException("agency not found"));
        Employe employe = employeeMapper.convertToEntity(employeeDTO);
        employe.setAgence(agence);
        return employeeMapper.convertToDTO(employeRepository.save(employe));

    }

    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Employe saved = employeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException("employee with id" + employeeId + "not found"));
        employeRepository.deleteById(saved.getId());
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employe> employees = employeRepository.findAll(); // get employees
        // method 1 : imperative
        List<EmployeeDTO> employeesDTO = new ArrayList<>(); // create a List of EmployeeDTO
        for (Employe employe:employees) { // we convert each employee to a dto
            employeesDTO.add(employeeMapper.convertToDTO(employe));
        }
       return employeesDTO; // we return the dto List
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employe saved = employeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException("employee with id" + employeeId + "not found"));
        return employeeMapper.convertToDTO(saved);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByAgency(Long agencyId) throws AgencyNotFoundException {
        Agence agence = agencyrepository.findById(agencyId).orElseThrow(() -> new AgencyNotFoundException("agency not found"));
        List<Employe> employees = employeRepository.findByAgenceId(agence.getId());
        // method 2 : fonctionel
        List<EmployeeDTO> employeesDTO = employees.stream()
                .map(employeeMapper::convertToDTO)
                .collect(Collectors.toList());
        return employeesDTO;
    }

}