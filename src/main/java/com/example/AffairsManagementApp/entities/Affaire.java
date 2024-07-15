package com.example.AffairsManagementApp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
// this class is associated to an agency, an employee , and an operation

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Affaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    // FK

    private String codeAgence; // Numérique, Obligatoire

    @ManyToOne
    private Agence agence; // FK
    @ManyToOne
    private Employe employe; // FK

    @OneToMany(mappedBy = "affaire")
    private List<Operation> operations;

    // Beneficiaire details
    private String typeFinancement; // Alphanumérique, Obligatoire
    private String typeIntervention; // Alphanumérique, Obligatoire
    private String cible; // Alphanumérique, Obligatoire
    private String nomBeneficiaire; // Chaine de caractères, Obligatoire
    private String prenomBeneficiaire; // Chaine de caractères, Obligatoire
    private String numeroCNIEBeneficiaire; // Alphanumérique, Obligatoire
    private String genreBeneficiaire; // Alphanumérique, Obligatoire
    @Temporal(TemporalType.DATE)
    private Date dateDeNaissanceBeneficiaire; // Date, format jj/mm/aaaa, Obligatoire

    // Financement details
    private String numeroFinancementBanque; // Chaîne de caractères, Obligatoire
    private String objetDuFinancement; // Alphanumérique, Obligatoire
    private Integer montantDuFinancement; // Nombre entier, Obligatoire
    private Integer coutAcquisition; // Nombre entier, Obligatoire
    private Double tauxDeMarge; // Nombre décimal, Obligatoire
    private Boolean margeSurDiffere; // 'O' ou 'N', Obligatoire
    private Integer apportDuBeneficiaire; // Nombre entier, Obligatoire
    private Integer prixLogement; // Nombre entier, Obligatoire
    private Integer duree; // Nombre entier, Obligatoire

    // Logement details
    private String numeroTF; // Chaine de caractère, Obligatoire
    private String natureDuTF; // Alphanumérique, Obligatoire
    private Boolean acquisitionIndivision; // 'O' ou 'N', Alphanumérique, Obligatoire
    private String adresseLogement; // Chaine de caractère, Obligatoire
    private String codeVille; // Alphanumérique, Obligatoire
    private Integer superficie; // Nombre entier, Obligatoire
    private String vendeurLogement; // Alphanumérique, Obligatoire

    // Co-Indivisaire details (optional)
    private String nomCoIndivisaire; // Chaine de caractère
    private String prenomCoIndivisaire; // Chaine de caractère
    private String numeroCNIECoIndivisaire; // Alphanumérique
    private String genreCoIndivisaire; // Alphanumérique
    private String liaisonFamilialeCoIndivisaire; // Alphanumérique

}

