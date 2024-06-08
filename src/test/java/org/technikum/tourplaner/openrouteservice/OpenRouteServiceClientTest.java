package org.technikum.tourplaner.openrouteservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenRouteServiceClientTest {
    @Test
    void openTourMap() {
        String start = "AT, 1230 Vienna, Rößlergasse";
        String end = "AT, 1220 Vienna, Farngasse";

        OpenRouteServiceClient openRouteServiceClient = new OpenRouteServiceClient();
        String directions = openRouteServiceClient.getTourInformation(start, end, "driving-car");

        openRouteServiceClient.openTourMapInBrowser(directions);
    }
}