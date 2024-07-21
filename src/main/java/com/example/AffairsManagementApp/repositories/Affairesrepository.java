package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Affair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Affairesrepository extends JpaRepository<Affair,Long > {
}
