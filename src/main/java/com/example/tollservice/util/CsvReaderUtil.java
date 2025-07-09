package com.example.tollservice.util;

import com.example.tollservice.model.TollPlaza;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvReaderUtil {

    private List<TollPlaza> tollPlazas = new ArrayList<>();

    @PostConstruct
    public void loadCsvData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("toll_plazas.csv")
        ))) {
            tollPlazas = reader.lines()
                    .skip(1) // skip header
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new TollPlaza(
                                Double.parseDouble(parts[0]),
                                Double.parseDouble(parts[1]),
                                parts[2].trim(),
                                parts[3].trim()
                        );
                    })
                    .collect(Collectors.toList());
            System.out.println("Loaded Toll Plazas: " + tollPlazas.size());
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }

    public List<TollPlaza> getTollPlazas() {
        return tollPlazas;
    }
}
