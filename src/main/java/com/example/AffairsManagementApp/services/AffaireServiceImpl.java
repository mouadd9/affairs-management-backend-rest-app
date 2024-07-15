package com.example.AffairsManagementApp.services;

import com.example.AffairsManagementApp.Exceptions.AffairNotFoundException;
import com.example.AffairsManagementApp.entities.Affaire;
import com.example.AffairsManagementApp.entities.Operation;
import com.example.AffairsManagementApp.enums.OperationTypes;
import com.example.AffairsManagementApp.repositories.Affairesrepository;
import com.example.AffairsManagementApp.repositories.Operationsrepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AffaireServiceImpl implements AffaireService {

    private final Affairesrepository affairesrepository;
    private final Operationsrepository operationsrepository;

    @Override
    public Affaire saveAffaire(Affaire affaire) {
        Operation operation = new Operation();
        operation.setAffaire(affaire); // to put affaireId as a FK
        operation.setDateOperation(new Date());
        operation.setOperation(OperationTypes.AJOUT);
        operationsrepository.save(operation);
        Affaire saved = affairesrepository.save(affaire);
        return saved;
        // here we should add an agency/employee id to the affaire and an employee id
        // to the operation
    }

    @Override
    public List<Affaire> getAllAffairs() {
        Operation operation = new Operation();
        operation.setAffaire(null);
        operation.setOperation(OperationTypes.CONSULTATION);
        operation.setDateOperation(new Date());
        operationsrepository.save(operation);
        List<Affaire> affaires = affairesrepository.findAll();
        return affaires;
    }

    @Override
    public Affaire getAffaireById(Long affaireId) throws AffairNotFoundException {
        Affaire affaire = affairesrepository.findById(affaireId).orElse(null);
        if(affaire == null){
            throw new AffairNotFoundException("Affair with ID " + affaireId + " not found.");
        }
        Operation operation = new Operation();
        operation.setOperation(OperationTypes.CONSULTATIONBYID);
        operation.setAffaire(affaire);
        operation.setDateOperation(new Date());
        operationsrepository.save(operation);
        return affaire;
    }

    @Override
    public void deleteAffaire(Long affaireId) throws AffairNotFoundException {
        Affaire affaire = this.getAffaireById(affaireId);
        Operation operation = new Operation();
        operation.setOperation(OperationTypes.SUPPRESSION);
        operation.setAffaire(affaire);
        operation.setDateOperation(new Date());
        operationsrepository.save(operation);
        affairesrepository.deleteById(affaireId);
    }
}
