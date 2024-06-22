package org.technikum.tourplaner.openrouteservice;

import org.junit.jupiter.api.Test;

class OpenRouteServiceClientTest {
    @Test
    void openTourMap() {
        String start = "AT, 1220 Vienna, Markweg";
        String end = "AT, 1220 Vienna, Farngasse";

        OpenRouteServiceClient openRouteServiceClient = new OpenRouteServiceClient();
        String directions = openRouteServiceClient.getTourInformation(start, end, ETransportType.Cycling);

        OpenRouteServiceClient.openTourMapInBrowser(directions);
    }
}