package com.example.tollservice.util;

import com.example.tollservice.dto.LatLng;
import com.example.tollservice.util.PincodeLatLngUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;

@Component
public class MapplsApiClient {

    private final String clientId;
    private final String clientSecret;

    private String accessToken;
    private Instant tokenExpiry;

    private static final String OAUTH_URL = "https://outpost.mappls.com/api/security/oauth/token";
    private static final String GEOCODE_URL = "https://atlas.mappls.com/api/places/geocode?address=%s&region=IND";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${mappls.api.key}")
    private String mapplsApiKey;

    public MapplsApiClient(
        @Value("${mappls.client.id}") String clientId,
        @Value("${mappls.client.secret}") String clientSecret
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private void fetchAccessToken() throws Exception {
        System.out.println("[DEBUG] Using clientId: " + clientId);
        System.out.println("[DEBUG] Using clientSecret: " + clientSecret);
        if (accessToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            return; // Token is still valid
        }
        String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", clientId, clientSecret);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(OAUTH_URL, entity, String.class);
        JsonNode json = mapper.readTree(response);
        this.accessToken = json.get("access_token").asText();
        int expiresIn = json.get("expires_in").asInt();
        this.tokenExpiry = Instant.now().plusSeconds(expiresIn - 60); // buffer
        System.out.println("[DEBUG] Fetched new access token");
    }

    public LatLng getLatLngFromPincode(String pincode) {
        System.out.println("[DEBUG] getLatLngFromPincode called for pincode: " + pincode);
        LatLng latLng = PincodeLatLngUtil.getLatLngForPincode(pincode);
        if (latLng != null) {
            System.out.println("[DEBUG] Found lat/lng for pincode " + pincode + ": " + latLng.getLatitude() + ", " + latLng.getLongitude());
            return latLng;
        } else {
            System.out.println("[DEBUG] No lat/lng found for pincode " + pincode + " in static CSV");
            return null;
        }
    }

    public JsonNode getRouteGeoJson(LatLng source, LatLng dest) throws Exception {
        // Use API key-based endpoint as per Mappls documentation
        String url = String.format(
            "https://apis.mappls.com/advancedmaps/v1/%s/route_geojson?start=%f,%f&end=%f,%f&geometries=polyline&overview=full",
            mapplsApiKey, source.getLongitude(), source.getLatitude(), dest.getLongitude(), dest.getLatitude()
        );
        System.out.println("[DEBUG] Routing API URL: " + url);
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("[DEBUG] Routing API response: " + response);
            return mapper.readTree(response);
        } catch (Exception e) {
            System.out.println("[DEBUG] Exception in getRouteGeoJson: " + e.getMessage());
            throw e;
        }
    }
}
