package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Agency;
import com.example.AffairsManagementApp.entities.AppUser;
import com.example.AffairsManagementApp.entities.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDetailsrepository extends JpaRepository<EmployeeDetails,Long> {
    // here we will fetch for an EmployeeDetails object that has a user with a specific userId
    // the column userId references a AppUser entity and the column agencyId references an Agency entity
    Optional<EmployeeDetails> findByAppUserId(Long userId);
    Optional<EmployeeDetails> findByAppUser(AppUser appUser);
}
