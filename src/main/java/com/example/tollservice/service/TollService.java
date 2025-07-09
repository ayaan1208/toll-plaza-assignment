package com.example.tollservice.service;

import com.example.tollservice.dto.TollPlazaDto;
import com.example.tollservice.dto.TollPlazaResponse;
import com.example.tollservice.dto.RouteDto;
import com.example.tollservice.dto.TollPlazaRequest;
import com.example.tollservice.exception.BadRequestException;
import com.example.tollservice.model.TollPlaza;
import com.example.tollservice.util.CsvReaderUtil;
import com.example.tollservice.util.DistanceUtil;
import com.example.tollservice.util.GoogleDirectionsApiClient;
import com.example.tollservice.util.PolylineDecoder;
import com.example.tollservice.util.PincodeLatLngUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TollService {

    private final CsvReaderUtil csvReaderUtil;
    private final GoogleDirectionsApiClient googleDirectionsApiClient;

    public TollService(CsvReaderUtil csvReaderUtil, GoogleDirectionsApiClient googleDirectionsApiClient) {
        this.csvReaderUtil = csvReaderUtil;
        this.googleDirectionsApiClient = googleDirectionsApiClient;
    }

    @Cacheable("tollRoutes")
    public TollPlazaResponse getTollPlazasBetween(TollPlazaRequest request) {
        String source = request.getSourcePincode();
        String destination = request.getDestinationPincode();

        if (source.equals(destination)) {
            throw new BadRequestException("Source and destination pincodes cannot be the same");
        }

        var sourceLatLng = PincodeLatLngUtil.getLatLngForPincode(source);
        var destLatLng = PincodeLatLngUtil.getLatLngForPincode(destination);
        double[] sourceCoords = sourceLatLng != null ? new double[]{sourceLatLng.getLatitude(), sourceLatLng.getLongitude()} : null;
        double[] destCoords = destLatLng != null ? new double[]{destLatLng.getLatitude(), destLatLng.getLongitude()} : null;

        if (sourceCoords == null || destCoords == null) {
            RouteDto route = new RouteDto(source, destination, 0);
            throw new BadRequestException("Invalid source or destination pincode", route, List.of());
        }

        List<double[]> routePoints = new ArrayList<>();
        try {
            String src = sourceCoords[0] + "," + sourceCoords[1];
            String dst = destCoords[0] + "," + destCoords[1];
            com.fasterxml.jackson.databind.JsonNode directions = googleDirectionsApiClient.getRoute(src, dst);
            com.fasterxml.jackson.databind.JsonNode routes = directions.path("routes");
            if (routes.isArray() && routes.size() > 0) {
                com.fasterxml.jackson.databind.JsonNode firstRoute = routes.get(0);
                com.fasterxml.jackson.databind.JsonNode overviewPolyline = firstRoute.path("overview_polyline");
                String polyline = overviewPolyline.path("points").asText();
                routePoints = PolylineDecoder.decodePoly(polyline);
            }
        } catch (Exception e) {
        }
        if (routePoints.isEmpty()) {
            RouteDto route = new RouteDto(source, destination, 0);
            throw new BadRequestException("No route found between the given pincodes", route, List.of());
        }

        double totalDistance = DistanceUtil.calculateTotalDistance(routePoints);

        List<TollPlazaDto> tollsOnRoute = new ArrayList<>();
        for (TollPlaza toll : csvReaderUtil.getTollPlazas()) {
            double minDistance = Double.MAX_VALUE;
            for (double[] point : routePoints) {
                double distance = DistanceUtil.haversine(point[0], point[1], toll.getLatitude(), toll.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
            if (minDistance <= 5.0) {
                double distFromSource = DistanceUtil.haversine(
                        sourceCoords[0], sourceCoords[1],
                        toll.getLatitude(), toll.getLongitude()
                );
                tollsOnRoute.add(new TollPlazaDto(toll.getTollName(), toll.getLatitude(), toll.getLongitude(), distFromSource));
            }
        }

        tollsOnRoute.sort(Comparator.comparingDouble(TollPlazaDto::getDistanceFromSource));

        if (tollsOnRoute.isEmpty()) {
            RouteDto route = new RouteDto(source, destination, (int) Math.round(totalDistance));
            throw new BadRequestException("No toll plazas found on the route", route, List.of());
        }

        return new TollPlazaResponse(
                new RouteDto(source, destination, (int) Math.round(totalDistance)),
                tollsOnRoute
        );
    }
}
