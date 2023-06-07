package com.example.ridesync.Classes;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class GoogleMapsCoordinates {

    // ENTER GOOGLE MAPS API KEY FOR JAVASCRIPT HERE
    private final static String API_KEY = "";

    public static GeocodingResult returnCoordinates(String address) {
        GeoApiContext context = (new GeoApiContext.Builder()).apiKey(API_KEY).build();

        try {
            GeocodingResult[] results = (GeocodingResult[]) GeocodingApi.geocode(context, address).await();
            if (results.length > 0) {
                GeocodingResult result = results[0];
                System.out.println("Formatted Address: " + result.formattedAddress);
                System.out.println("Latitude: " + result.geometry.location.lat);

                System.out.println("Longitude: " + result.geometry.location.lng);

                return result;
            } else {
                System.out.println("No results found.");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
