package com.example.tollservice.controller;

import com.example.tollservice.dto.TollPlazaRequest;
import com.example.tollservice.dto.TollPlazaResponse;
import com.example.tollservice.service.TollService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/toll-plazas")
public class TollController {

    private final TollService tollService;

    public TollController(TollService tollService) {
        this.tollService = tollService;
    }

    @PostMapping
    public ResponseEntity<TollPlazaResponse> getTollPlazas(@Valid @RequestBody TollPlazaRequest request) {
        TollPlazaResponse response = tollService.getTollPlazasBetween(request);
        return ResponseEntity.ok(response);
    }
}
