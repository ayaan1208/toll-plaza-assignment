package com.example.tollservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TollPlazaRequest {
    @NotBlank(message = "Source pincode cannot be blank")
    private String sourcePincode;

    @NotBlank(message = "Destination pincode cannot be blank")
    private String destinationPincode;
}
