package com.example.AffairsManagementApp.DTOs;

import com.example.AffairsManagementApp.enums.AgencyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank
    @Pattern(regexp = "^[0-9]+$", message = "Code agence must be numeric")
    private String agencyCode;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Status cannot be null")
    private AgencyStatus status;

    private String formattedCreationDate;  // Add this field


}
