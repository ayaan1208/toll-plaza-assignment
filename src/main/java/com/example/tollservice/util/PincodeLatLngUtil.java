package com.example.tollservice.util;

import com.example.tollservice.dto.LatLng;
import org.springframework.core.io.ClassPathResource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PincodeLatLngUtil {
    private static final Map<String, LatLng> PINCODE_MAP = new HashMap<>();

    static {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("pincode_latlng.csv").getInputStream()
            ));
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String pincode = parts[0].trim();
                    double lat = Double.parseDouble(parts[1].trim());
                    double lng = Double.parseDouble(parts[2].trim());
                    PINCODE_MAP.put(pincode, new LatLng(lat, lng));
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("[DEBUG] Failed to load pincode_latlng.csv: " + e.getMessage());
        }
    }

    public static LatLng getLatLngForPincode(String pincode) {
        return PINCODE_MAP.get(pincode);
    }
} 