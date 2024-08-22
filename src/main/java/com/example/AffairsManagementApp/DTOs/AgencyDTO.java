package com.example.AffairsManagementApp.DTOs;

import com.example.AffairsManagementApp.enums.AgencyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyDTO {
    private Long id;

    @NotNull(message = "Agency code cannot be null")
    private String agencyCode;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Status cannot be null")
    private AgencyStatus status;

    private String formattedCreationDate;  // Add this field


}
