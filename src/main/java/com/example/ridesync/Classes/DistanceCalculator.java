package com.example.ridesync.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

public class DistanceCalculator {
    // ENTER GOOGLE MAPS API KEY FOR JAVASCRIPT HERE
    private static final String API_KEY = "";
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();

    public static double getDistance(String origin, String destination) {
        try {
            DirectionsApiRequest req = DirectionsApi.newRequest(context)
                    .origin(origin)
                    .destination(destination)
                    .mode(TravelMode.DRIVING)
                    .avoid(RouteRestriction.TOLLS);
            DirectionsResult res = req.await();
            DirectionsRoute route = res.routes[0];
            return route.legs[0].distance.inMeters / 1000.0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static List<Distance> getDistances(String userLoc, ArrayList<String> driverLoc) {
        List<Distance> distances = new ArrayList<>();
        for (String address : driverLoc) {
            double distance = getDistance(userLoc, address);
            distances.add(new Distance(address, distance));
        }
        distances.sort(Comparator.comparing(Distance::getDistance));
        return distances;
    }

    public static void main(String[] args) {
        String userLoc = "1600 Amphitheatre Parkway, Mountain View, CA";
        ArrayList<String> driverLoc = new ArrayList<>();
        driverLoc.add("1 Hacker Way, Menlo Park, CA");
        driverLoc.add("345 Spear St, San Francisco, CA");
        driverLoc.add("1600 Pennsylvania Ave NW, Washington, DC");
        driverLoc.add("1830 E Park Ave, Tallahassee, FL");
        driverLoc.add("1 Microsoft Way, Redmond, WA");
        List<Distance> distances = getDistances(userLoc, driverLoc);
        for (Distance distance : distances) {
            System.out.println("Distance from " + userLoc + " to " + distance.getAddress() + ": " + distance.getDistance() + " km");
        }
    }
    private static class Distance {
        private final String address;
        private final double distance;

        public Distance(String address, double distance) {
            this.address = address;
            this.distance = distance;
        }

        public String getAddress() {
            return address;
        }

        public double getDistance() {
            return distance;
        }
    }
}


