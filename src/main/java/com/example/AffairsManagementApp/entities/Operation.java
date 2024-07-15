package com.example.AffairsManagementApp.entities;

import com.example.AffairsManagementApp.enums.OperationTypes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private Date dateOperation;
    @Enumerated(EnumType.STRING)
    private OperationTypes operation;
    @ManyToOne
    private Affaire affaire;
    @ManyToOne
    private Employe employe;
}
