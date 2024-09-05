package com.example.AffairsManagementApp.entities;

import com.example.AffairsManagementApp.enums.AgencyStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
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

    private String address;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private AgencyStatus status;

    @OneToMany(mappedBy = "agency")
    private Collection<Affair> affairs = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "agency")
    private Collection<EmployeeDetails> employeeDetailsList = new HashSet<>();



}
