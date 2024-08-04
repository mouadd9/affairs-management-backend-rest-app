package com.example.AffairsManagementApp.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyDTO {
    private Long id;

    @NotNull(message = "Agency code cannot be null")
    private String agencyCode;


}
