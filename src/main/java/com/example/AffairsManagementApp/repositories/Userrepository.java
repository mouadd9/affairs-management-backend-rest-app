package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Userrepository extends JpaRepository<Utilisateur, Long> {
}
