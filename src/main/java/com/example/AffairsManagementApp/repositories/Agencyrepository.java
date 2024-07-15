package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Agencyrepository extends JpaRepository<Agence , Long> {
}
