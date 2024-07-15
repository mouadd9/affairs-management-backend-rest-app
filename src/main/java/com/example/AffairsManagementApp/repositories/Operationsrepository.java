package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Operationsrepository extends JpaRepository<Operation,Long> {
}
