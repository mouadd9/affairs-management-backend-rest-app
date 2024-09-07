package com.example.AffairsManagementApp.services.implementations;

import com.example.AffairsManagementApp.DTOs.AffairDTO;
import com.example.AffairsManagementApp.Exceptions.AffairNotFoundException;
import com.example.AffairsManagementApp.Exceptions.AgencyNotFoundException;
import com.example.AffairsManagementApp.entities.Affair;
import com.example.AffairsManagementApp.mappers.AffairMapper;
import com.example.AffairsManagementApp.repositories.Affairsrepository;
import com.example.AffairsManagementApp.repositories.Agencyrepository;
import com.example.AffairsManagementApp.services.interfaces.AffairService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AffairServiceImpl implements AffairService {

    private final Affairsrepository affairsrepository;
    private final Agencyrepository agencyrepository;
    private final AffairMapper affairMapper;


    @Override
    public AffairDTO createAffair(AffairDTO affairDto) throws AgencyNotFoundException {
        // here I tried to use a functional approach for the purpose of learning

        return Optional.ofNullable(affairDto.getCodeAgence())
                .map(this.agencyrepository::findByAgencyCode)
                // if the agency exists we pass it
                .map(agency -> Optional.of(affairMapper.convertToEntity(affairDto))
                               // if the affair is converted we pass it to a nested map(it will set up the rlsp with the agency and then save the affair)
                                .map(affair -> {
                                    affair.setAgency(agency);
                                    agency.getAffairs().add(affair);
                                    return affairMapper.convertToDTO(this.affairsrepository.save(affair));
                                })
                                // if the affair failed to create we throw an error (unlikely)
                                .orElseThrow(() -> new RuntimeException("Failed to create affair"))
                )
                // if the agency doesn't exist we throw an error
                .orElseThrow(() -> new AgencyNotFoundException("Agency with code: " + affairDto.getCodeAgence() + " doesn't exist"));
    }

    @Override
    public Long getCount() {
        return this.affairsrepository.count();
    }

    @Override
    public List<AffairDTO> getAffairs() {
        return this.affairsrepository.findAll()
                .stream()
                .map(this.affairMapper::convertToDTO)
                .toList();
    }

    @Override
    public Long getCountByAgency(Long id) throws AgencyNotFoundException {
        return this.agencyrepository.findById(id)
                .map(this.affairsrepository::countByAgency)
                .orElseThrow(() -> new AgencyNotFoundException("Agency with id " + id + " doesn't exist"));

    }

    @Override
    public List<AffairDTO> getAffairsByAgency(Long id) throws AgencyNotFoundException {
        return this.agencyrepository.findById(id)
                .map(agency ->
                    this.affairsrepository.findByAgency(agency)
                            .stream()
                            .map(this.affairMapper::convertToDTO)
                            .toList()
                )
                .orElseThrow(() -> new AgencyNotFoundException("Agency with id " + id + " doesn't exist"));
    }

    @Override
    public AffairDTO updateAffair(AffairDTO affairDTO) throws AffairNotFoundException {
        return this.affairsrepository.findById(affairDTO.getId())
                .map(affair -> this.affairMapper.convertToDTO(
                        this.affairsrepository.save(
                                this.affairMapper.updateAffairFromDTO(affair, affairDTO)
                        )
                ))
                .orElseThrow(() -> new AffairNotFoundException("Affair with id " + affairDTO.getId() + " doesn't exist"));
    }

    @Override
    public void deleteAffair(Long id) throws AffairNotFoundException {
        Affair affair = this.affairsrepository.findById(id)
                .orElseThrow(() -> new AffairNotFoundException("Affair with id " + id + " doesn't exist"));
        this.affairsrepository.delete(affair);
    }

}
