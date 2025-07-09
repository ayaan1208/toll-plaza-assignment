package com.example.tollservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TollPlaza {
    private double longitude;
    private double latitude;
    private String tollName;
    private String geoState;
}
