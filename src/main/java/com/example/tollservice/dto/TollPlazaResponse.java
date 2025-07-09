package com.example.tollservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TollPlazaResponse {
    private RouteDto route;
    private List<TollPlazaDto> tollPlazas;
}
