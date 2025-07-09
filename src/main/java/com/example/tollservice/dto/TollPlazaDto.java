package com.example.tollservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TollPlazaDto {
    private String name;
    private double latitude;
    private double longitude;
    private double distanceFromSource;
}
