package com.example.AffairsManagementApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(unique = true)
    private String agencyCode;

    @OneToMany(mappedBy = "agency")
    private Collection<Affair> affairs = new HashSet<>();

    @OneToMany(mappedBy = "agency")
    private Collection<EmployeeDetails> employeeDetailsList = new HashSet<>();

}
