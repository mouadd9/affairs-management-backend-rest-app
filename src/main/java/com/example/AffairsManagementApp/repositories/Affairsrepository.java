package com.example.AffairsManagementApp.repositories;

import com.example.AffairsManagementApp.entities.Affair;
import com.example.AffairsManagementApp.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Affairsrepository extends JpaRepository<Affair,Long > {
    // here we count the number of affairs that have the same agency_id(fk) as the id(pk) in agency
    /*
      SELECT COUNT(*)
      FROM affair
      WHERE agency_id = ?
    */
    Long countByAgency(Agency agency);

    /*
      SELECT *
      FROM affair
      WHERE agency_id = ?
    */

    List<Affair> findByAgency(Agency agency);


}
