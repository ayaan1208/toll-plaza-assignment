package com.example.tollservice.util;

import com.example.tollservice.dto.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RouteUtils {

    public static List<LatLng> decodePolyline(String encoded) {
        List<LatLng> path = new ArrayList<>();
        int index = 0, lat = 0, lng = 0;

        while (index < encoded.length()) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng(lat / 1E5, lng / 1E5);
            path.add(position);
        }
        return path;
    }

    public static double haversineDistance(LatLng p1, LatLng p2) {
        final int R = 6371; // Radius of earth in KM
        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double lat1 = Math.toRadians(p1.getLatitude());
        double lat2 = Math.toRadians(p2.getLatitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2)
                * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

    public static boolean isWithinDistance(LatLng point, List<LatLng> route, double radiusKm) {
        for (LatLng routePoint : route) {
            if (haversineDistance(point, routePoint) <= radiusKm) return true;
        }
        return false;
    }
}
