package com.example.AffairsManagementApp.controllers;

import com.example.AffairsManagementApp.DTOs.EmployeeDTO;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.Exceptions.EmployeeNotFoundException;
import com.example.AffairsManagementApp.entities.Employe;
import com.example.AffairsManagementApp.services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeesService employeesService;

    // POST: Add a new employee to an agency
    @PostMapping("/agency/{agencyId}")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable (name = "agencyId") Long agencyId) {
        try {
            EmployeeDTO savedEmployee = employeesService.saveEmployeeByAgency(employeeDTO, agencyId);
            return ResponseEntity.ok(savedEmployee);
        } catch (AgencyNotFoundException e) {
            // if the agency is not found then we will send 400 status
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Remove an employee by ID
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        try {
            employeesService.deleteEmployee(employeeId);
            return ResponseEntity.noContent().build();
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET: Retrieve all employees
    @GetMapping("/")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        List<EmployeeDTO> employees =  employeesService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // GET: Retrieve employees by agency ID
    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByAgency(@PathVariable(name = "agencyId") Long agencyId){
        try {
            List<EmployeeDTO> employees =  employeesService.getEmployeesByAgency(agencyId);
            return ResponseEntity.ok(employees);
        } catch (AgencyNotFoundException e) {
            // if the agency is not found then we will send 400 status
            return ResponseEntity.notFound().build();
        }

    }

    // GET: Retrieve a single employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable(name = "employeeId") Long employeeId) {
       try { EmployeeDTO employee = employeesService.getEmployeeById(employeeId);
           return ResponseEntity.ok(employee);
       } catch (EmployeeNotFoundException e){
           return ResponseEntity.notFound().build();
       }
    }



}
