package com.example.tollservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteDto {
    private String sourcePincode;
    private String destinationPincode;
    private int distanceInKm;
}
