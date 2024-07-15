package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Affaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Affairesrepository extends JpaRepository<Affaire,Long > {
}
