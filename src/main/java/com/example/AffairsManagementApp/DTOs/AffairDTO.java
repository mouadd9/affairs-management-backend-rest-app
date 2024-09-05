package com.example.AffairsManagementApp.DTOs;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffairDTO {
    private Long id; // PK

    @NotBlank
    @Pattern(regexp = "^[0-9]+$", message = "Code agence must be numeric")
    private String codeAgence; // Numérique, Obligatoire

    // Beneficiaire details
    @NotBlank
    private String typeFinancement; // Alphanumérique, Obligatoire

    @NotBlank
    private String typeIntervention; // Alphanumérique, Obligatoire

    @NotBlank
    private String cible; // Alphanumérique, Obligatoire


    @NotBlank
    private String nomBeneficiaire; // Chaine de caractères, Obligatoire

    @NotBlank
    private String prenomBeneficiaire; // Chaine de caractères, Obligatoire

    @NotBlank
    private String numeroCNIEBeneficiaire; // Alphanumérique, Obligatoire

    @NotBlank
    private String genreBeneficiaire; // Alphanumérique, Obligatoire

    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    private Date dateDeNaissanceBeneficiaire; // Date, format jj/mm/aaaa, Obligatoire

    // Financement details
    @NotBlank
    private String numeroFinancementBanque; // Chaîne de caractères, Obligatoire

    @NotBlank
    private String objetDuFinancement; // Alphanumérique, Obligatoire

    @NotNull
    @Positive
    private Integer montantDuFinancement; // Nombre entier, Obligatoire

    @NotNull
    @Positive
    private Integer coutAcquisition; // Nombre entier, Obligatoire

    @NotNull
    @Positive
    private Double tauxDeMarge; // Nombre décimal, Obligatoire

    @NotNull
    private Boolean margeSurDiffere; // 'O' ou 'N', Obligatoire

    @NotNull
    @PositiveOrZero
    private Integer apportDuBeneficiaire; // Nombre entier, Obligatoire

    @NotNull
    @Positive
    private Integer prixLogement; // Nombre entier, Obligatoire

    @NotNull
    @Positive
    private Integer duree; // Nombre entier, Obligatoire

    // Logement details
    @NotBlank
    private String numeroTF; // Chaine de caractère, Obligatoire

    @NotBlank
    private String natureDuTF; // Alphanumérique, Obligatoire

    @NotNull
    private Boolean acquisitionIndivision; // 'O' ou 'N', Alphanumérique, Obligatoire

    @NotBlank
    private String adresseLogement; // Chaine de caractère, Obligatoire

    @NotBlank
    private String codeVille; // Alphanumérique, Obligatoire

    @NotNull
    @Positive
    private Integer superficie; // Nombre entier, Obligatoire

    @NotBlank
    private String vendeurLogement; // Alphanumérique, Obligatoire

    // Co-Indivisaire details (optional)
    private String nomCoIndivisaire; // Chaine de caractère
    private String prenomCoIndivisaire; // Chaine de caractère
    private String numeroCNIECoIndivisaire; // Alphanumérique
    private String genreCoIndivisaire; // Alphanumérique
    private String liaisonFamilialeCoIndivisaire; // Alphanumérique


}
