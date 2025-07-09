package com.example.tollservice;

import com.example.tollservice.util.CsvReaderUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TollserviceApplication implements CommandLineRunner {

    private final CsvReaderUtil csvReaderUtil;

    public TollserviceApplication(CsvReaderUtil csvReaderUtil) {
        this.csvReaderUtil = csvReaderUtil;
    }

    public static void main(String[] args) {
        SpringApplication.run(TollserviceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Total Toll Plazas: " + csvReaderUtil.getTollPlazas().size());
    }
}
