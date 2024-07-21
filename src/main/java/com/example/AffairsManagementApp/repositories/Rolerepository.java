package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Rolerepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
