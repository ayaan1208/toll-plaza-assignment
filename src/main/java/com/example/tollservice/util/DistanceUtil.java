package com.example.tollservice.util;

public class DistanceUtil {
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public static double calculateTotalDistance(java.util.List<double[]> points) {
        double total = 0.0;
        for (int i = 1; i < points.size(); i++) {
            double[] prev = points.get(i - 1);
            double[] curr = points.get(i);
            total += haversine(prev[0], prev[1], curr[0], curr[1]);
        }
        return total;
    }
}
