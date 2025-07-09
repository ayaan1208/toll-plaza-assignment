package com.example.tollservice.util;

import com.example.tollservice.util.MapplsApiClient;
import com.example.tollservice.dto.LatLng;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class MapplsApiUtil {
    public static String buildRouteUrl(double srcLat, double srcLng, double destLat, double destLng, String apiKey) {
        return "https://apis.mappls.com/advancedmaps/v1/" + apiKey +
               "/route?start=" + srcLng + "," + srcLat +
               "&end=" + destLng + "," + destLat;
    }
}
