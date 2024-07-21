package com.example.AffairsManagementApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class EmployeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK


    @OneToOne
    @JoinColumn(name = "appUser_id", nullable = false)
    private AppUser appUser; // FK

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency; // FK


}
