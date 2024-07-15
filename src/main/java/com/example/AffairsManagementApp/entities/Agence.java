package com.example.AffairsManagementApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK
    private String name;

    @OneToMany(mappedBy = "agence")
    private List<Affaire> affaires;

    @OneToMany(mappedBy = "agence")
    private List<Employe> employes;

}
