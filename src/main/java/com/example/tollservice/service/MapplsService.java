package com.example.tollservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class MapplsService {

    @Value("${mappls.api.key}")
    private String mapplsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<double[]> getRouteCoordinates(String sourcePincode, String destinationPincode) {
        // Geocoding Source
        double[] sourceCoord = getCoordinatesFromPincode(sourcePincode);
        double[] destinationCoord = getCoordinatesFromPincode(destinationPincode);

        if (sourceCoord == null || destinationCoord == null) {
            throw new IllegalArgumentException("Invalid source or destination pincode");
        }

        // Build Directions API URL
        String url = UriComponentsBuilder.fromHttpUrl("https://apis.mappls.com/advancedmaps/v1/" + mapplsApiKey + "/route_geojson")
                .queryParam("start", sourceCoord[1] + "," + sourceCoord[0])
                .queryParam("end", destinationCoord[1] + "," + destinationCoord[0])
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<double[]> routeCoordinates = new ArrayList<>();

        if (response != null && response.containsKey("routes")) {
            List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
            if (!routes.isEmpty()) {
                Map<String, Object> firstRoute = routes.get(0);
                Map<String, Object> geometry = (Map<String, Object>) firstRoute.get("geometry");
                List<List<Double>> coordinates = (List<List<Double>>) geometry.get("coordinates");

                for (List<Double> point : coordinates) {
                    routeCoordinates.add(new double[]{point.get(1), point.get(0)}); // lat, lon
                }
            }
        }

        return routeCoordinates;
    }

    private double[] getCoordinatesFromPincode(String pincode) {
        String url = "https://atlas.mappls.com/api/places/geocode?address=" + pincode + "&key=" + mapplsApiKey;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            if (!results.isEmpty()) {
                Map<String, Object> location = results.get(0);
                double lat = Double.parseDouble(location.get("latitude").toString());
                double lon = Double.parseDouble(location.get("longitude").toString());
                return new double[]{lat, lon};
            }
        }
        return null;
    }
}
