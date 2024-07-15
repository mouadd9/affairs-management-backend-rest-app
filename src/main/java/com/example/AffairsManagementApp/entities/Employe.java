package com.example.AffairsManagementApp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("EMPLOYEE")
@Data @AllArgsConstructor @NoArgsConstructor
public class Employe extends Utilisateur {

    @OneToMany(mappedBy = "employe")
    private List<Affaire> affaires;

    @OneToMany(mappedBy = "employe")
    private List<Operation> operations;

    @ManyToOne
    private Agence agence; // FK


}
