package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Employe;
import com.example.AffairsManagementApp.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Employerepository extends JpaRepository<Employe, Long> {
    List<Employe> findByAgenceId(Long agenceId);
}
