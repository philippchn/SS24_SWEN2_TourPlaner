package org.technikum.tourplaner.openrouteservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OpenRouteServiceClient {
    private static final Logger logger = LogManager.getLogger(OpenRouteServiceClient.class);

    private static final String API_URL_SEARCH = "https://api.openrouteservice.org/geocode/search";
    private static final String API_URL_DIRECTIONS = "https://api.openrouteservice.org/v2/directions/";
    private static final String API_KEY = "5b3ce3597851110001cf6248100ea82416bb4259b7387ad2777a08cb"; // TODO REPLACE WHEN MAKING REPO PUBLIC

    public String getTourInformation(String startAddress, String endAddress, ETransportType transportType){
        if (transportType == null) {
            logger.warn("Transport type is empty");
            throw new IllegalArgumentException();
        }
        AddressData startAddressData = getAddressDataFromApi(startAddress);
        AddressData endAddressData = getAddressDataFromApi(endAddress);

//        String tileNrCalculator = startAddressData.longitude + " " + startAddressData.latitude + " " + endAddressData.longitude + " " + endAddressData.latitude // TODO GENERATE IMAGE

        return getDirectionsFromApi(startAddressData, endAddressData, transportType);
    }

    private AddressData getAddressDataFromApi(String address) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String encodedText = URLEncoder.encode(address, StandardCharsets.UTF_8);
            URI uri = new URI(API_URL_SEARCH + "?api_key=" + API_KEY + "&text=" + encodedText);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(response.body());

            if(rootNode.path("features").isEmpty()){
                logger.warn("To/From destination " + address + " not found!");
                throw new IllegalArgumentException();
            }

            JsonNode coordinatesNode = rootNode.path("features").get(0).path("geometry").path("coordinates");
            Double longitude = coordinatesNode.get(0).asDouble();
            Double latitude = coordinatesNode.get(1).asDouble();
            return new AddressData(
                    address,
                    longitude,
                    latitude
            );

        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.warn("Error while parsing Adress: " + address);
            throw new IllegalArgumentException();
        }
    }

    private String getDirectionsFromApi(AddressData startAddressData, AddressData endAddressData, ETransportType transportType) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(API_URL_DIRECTIONS + transportType.getApiParameter()
                    + "?api_key=" + API_KEY
                    + "&start=" + startAddressData.longitude + "," + startAddressData.latitude
                    + "&end=" + endAddressData.longitude + "," + endAddressData.latitude);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            return response.body();
        } catch (Exception e) {
            logger.warn("Error while trying to get directions for start: " + startAddressData.name + " end: " + endAddressData.name);
            throw new IllegalArgumentException();
        }
    }

    public static void openTourMapInBrowser(String directions) {
        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Leaflet Map with Directions</title>
                <script>
                    var directions = %s
                </script>
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
                      integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
                      crossorigin=""/>
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
                        integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
                        crossorigin=""></script>
            </head>
            <body style="margin: 0; padding: 0;">
            <div id="map" style="height: 100vh"></div>
            <script>
                var map = L.map('map');
                var bbox = directions.bbox;
                map.fitBounds([[bbox[1], bbox[0]], [bbox[3], bbox[2]]]);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '2024 © FH Technikum Wien'
                }).addTo(map);
                L.geoJSON(directions).addTo(map);
            </script>
            </body>
            </html>
    """, directions);
        try{
            File htmlFile = File.createTempFile("leaflet_map", ".html");
            FileWriter writer = new FileWriter(htmlFile);
            writer.write(htmlContent);
            writer.close();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(htmlFile.toURI());
            } else {
                logger.warn("Desktop browsing not supported");
            }
        }catch (Exception e) {
            logger.warn("Error while creating HTML Leaflet file");
            throw new IllegalArgumentException();
        }
    }
}
