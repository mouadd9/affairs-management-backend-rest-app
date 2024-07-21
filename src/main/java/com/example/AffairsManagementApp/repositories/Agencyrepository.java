package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Agencyrepository extends JpaRepository<Agency, Long> {

}
