package com.example.tollservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleDirectionsApiClient {
    @Value("${google.api.key}")
    private String googleApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode getRoute(String sourceLatLng, String destLatLng) throws Exception {
        // sourceLatLng and destLatLng are in the format "lat,lng"
        String url = String.format(
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
            sourceLatLng, destLatLng, googleApiKey
        );
        System.out.println("[DEBUG] Google Directions API URL: " + url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("[DEBUG] Google Directions API response: " + response);
            return mapper.readTree(response);
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception in getRoute: " + e.getMessage());
            throw e;
        }
    }
} 