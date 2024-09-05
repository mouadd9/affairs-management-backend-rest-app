package com.example.AffairsManagementApp.mappers;

import com.example.AffairsManagementApp.DTOs.AffairDTO;
import com.example.AffairsManagementApp.entities.Affair;
import org.springframework.stereotype.Service;

@Service
public class AffairMapper {

   // this will convert an affair to a dto so it can be sent to the front end
    // affairDTO is identical to Affair the only diff is that we dont have the Agency (fk)
    public AffairDTO convertToDTO(Affair affair) {

        if (affair == null) {
            return null;
        }

        AffairDTO dto = new AffairDTO(); // here we create a new dto instance
        // then we map
        dto.setId(affair.getId());
        dto.setCodeAgence(affair.getCodeAgence());
        dto.setTypeFinancement(affair.getTypeFinancement());
        dto.setTypeIntervention(affair.getTypeIntervention());
        dto.setCible(affair.getCible());
        dto.setNomBeneficiaire(affair.getNomBeneficiaire());
        dto.setPrenomBeneficiaire(affair.getPrenomBeneficiaire());
        dto.setNumeroCNIEBeneficiaire(affair.getNumeroCNIEBeneficiaire());
        dto.setGenreBeneficiaire(affair.getGenreBeneficiaire());
        dto.setDateDeNaissanceBeneficiaire(affair.getDateDeNaissanceBeneficiaire());
        dto.setNumeroFinancementBanque(affair.getNumeroFinancementBanque());
        dto.setObjetDuFinancement(affair.getObjetDuFinancement());
        dto.setMontantDuFinancement(affair.getMontantDuFinancement());
        dto.setCoutAcquisition(affair.getCoutAcquisition());
        dto.setTauxDeMarge(affair.getTauxDeMarge());
        dto.setMargeSurDiffere(affair.getMargeSurDiffere());
        dto.setApportDuBeneficiaire(affair.getApportDuBeneficiaire());
        dto.setPrixLogement(affair.getPrixLogement());
        dto.setDuree(affair.getDuree());
        dto.setNumeroTF(affair.getNumeroTF());
        dto.setNatureDuTF(affair.getNatureDuTF());
        dto.setAcquisitionIndivision(affair.getAcquisitionIndivision());
        dto.setAdresseLogement(affair.getAdresseLogement());
        dto.setCodeVille(affair.getCodeVille());
        dto.setSuperficie(affair.getSuperficie());
        dto.setVendeurLogement(affair.getVendeurLogement());
        dto.setNomCoIndivisaire(affair.getNomCoIndivisaire());
        dto.setPrenomCoIndivisaire(affair.getPrenomCoIndivisaire());
        dto.setNumeroCNIECoIndivisaire(affair.getNumeroCNIECoIndivisaire());
        dto.setGenreCoIndivisaire(affair.getGenreCoIndivisaire());
        dto.setLiaisonFamilialeCoIndivisaire(affair.getLiaisonFamilialeCoIndivisaire());

        return dto;
    }

    // here we convert a dto to an entity ( used for creation operations)
    // when we create a new affair , we send data using a dto , then we convert the dto into an entity using this method
    //- creation - mapping -
    public Affair convertToEntity(AffairDTO dto) {
        if (dto == null) {
            return null;
        }

        Affair affair = new Affair();

        // Only set the ID if it's not null (for updates, not for new affairs)
        if (dto.getId() != null) {
            affair.setId(dto.getId());
        }

        affair.setCodeAgence(dto.getCodeAgence());
        affair.setTypeFinancement(dto.getTypeFinancement());
        affair.setTypeIntervention(dto.getTypeIntervention());
        affair.setCible(dto.getCible());
        affair.setNomBeneficiaire(dto.getNomBeneficiaire());
        affair.setPrenomBeneficiaire(dto.getPrenomBeneficiaire());
        affair.setNumeroCNIEBeneficiaire(dto.getNumeroCNIEBeneficiaire());
        affair.setGenreBeneficiaire(dto.getGenreBeneficiaire());
        affair.setDateDeNaissanceBeneficiaire(dto.getDateDeNaissanceBeneficiaire());
        affair.setNumeroFinancementBanque(dto.getNumeroFinancementBanque());
        affair.setObjetDuFinancement(dto.getObjetDuFinancement());
        affair.setMontantDuFinancement(dto.getMontantDuFinancement());
        affair.setCoutAcquisition(dto.getCoutAcquisition());
        affair.setTauxDeMarge(dto.getTauxDeMarge());
        affair.setMargeSurDiffere(dto.getMargeSurDiffere());
        affair.setApportDuBeneficiaire(dto.getApportDuBeneficiaire());
        affair.setPrixLogement(dto.getPrixLogement());
        affair.setDuree(dto.getDuree());
        affair.setNumeroTF(dto.getNumeroTF());
        affair.setNatureDuTF(dto.getNatureDuTF());
        affair.setAcquisitionIndivision(dto.getAcquisitionIndivision());
        affair.setAdresseLogement(dto.getAdresseLogement());
        affair.setCodeVille(dto.getCodeVille());
        affair.setSuperficie(dto.getSuperficie());
        affair.setVendeurLogement(dto.getVendeurLogement());
        affair.setNomCoIndivisaire(dto.getNomCoIndivisaire());
        affair.setPrenomCoIndivisaire(dto.getPrenomCoIndivisaire());
        affair.setNumeroCNIECoIndivisaire(dto.getNumeroCNIECoIndivisaire());
        affair.setGenreCoIndivisaire(dto.getGenreCoIndivisaire());
        affair.setLiaisonFamilialeCoIndivisaire(dto.getLiaisonFamilialeCoIndivisaire());

        // Note: Agency is not set here as it's not part of the DTO
        // You'll need to set it separately when creating a new Affair

        return affair; // here we return the affair (not saved to the database)
        // next step is using the agency code to retrieve the agency we want to add the affair to
        // then we add it to the affair
        // then we add affair to the agency
        // then we save
    }

    public Affair updateAffairFromDTO(Affair existingAffair, AffairDTO dto) {
        if (existingAffair == null || dto == null) {
            return existingAffair;
        }

        // Update all fields except id, codeAgence, and agency
        existingAffair.setTypeFinancement(dto.getTypeFinancement());
        existingAffair.setTypeIntervention(dto.getTypeIntervention());
        existingAffair.setCible(dto.getCible());
        existingAffair.setNomBeneficiaire(dto.getNomBeneficiaire());
        existingAffair.setPrenomBeneficiaire(dto.getPrenomBeneficiaire());
        existingAffair.setNumeroCNIEBeneficiaire(dto.getNumeroCNIEBeneficiaire());
        existingAffair.setGenreBeneficiaire(dto.getGenreBeneficiaire());
        existingAffair.setDateDeNaissanceBeneficiaire(dto.getDateDeNaissanceBeneficiaire());
        existingAffair.setNumeroFinancementBanque(dto.getNumeroFinancementBanque());
        existingAffair.setObjetDuFinancement(dto.getObjetDuFinancement());
        existingAffair.setMontantDuFinancement(dto.getMontantDuFinancement());
        existingAffair.setCoutAcquisition(dto.getCoutAcquisition());
        existingAffair.setTauxDeMarge(dto.getTauxDeMarge());
        existingAffair.setMargeSurDiffere(dto.getMargeSurDiffere());
        existingAffair.setApportDuBeneficiaire(dto.getApportDuBeneficiaire());
        existingAffair.setPrixLogement(dto.getPrixLogement());
        existingAffair.setDuree(dto.getDuree());
        existingAffair.setNumeroTF(dto.getNumeroTF());
        existingAffair.setNatureDuTF(dto.getNatureDuTF());
        existingAffair.setAcquisitionIndivision(dto.getAcquisitionIndivision());
        existingAffair.setAdresseLogement(dto.getAdresseLogement());
        existingAffair.setCodeVille(dto.getCodeVille());
        existingAffair.setSuperficie(dto.getSuperficie());
        existingAffair.setVendeurLogement(dto.getVendeurLogement());
        existingAffair.setNomCoIndivisaire(dto.getNomCoIndivisaire());
        existingAffair.setPrenomCoIndivisaire(dto.getPrenomCoIndivisaire());
        existingAffair.setNumeroCNIECoIndivisaire(dto.getNumeroCNIECoIndivisaire());
        existingAffair.setGenreCoIndivisaire(dto.getGenreCoIndivisaire());
        existingAffair.setLiaisonFamilialeCoIndivisaire(dto.getLiaisonFamilialeCoIndivisaire());

        return existingAffair;
    }

}
