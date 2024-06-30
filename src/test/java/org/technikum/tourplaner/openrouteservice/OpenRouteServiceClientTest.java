package org.technikum.tourplaner.openrouteservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.technikum.tourplaner.DAL.openrouteservice.ETransportType;
import org.technikum.tourplaner.DAL.openrouteservice.OpenRouteServiceClient;

class OpenRouteServiceClientTest {
    @Test
    @Disabled("Only for manual run cus it opens the Browser")
    void openTourMap() {
        String start = "AT, 1210 Vienna, Bahnhof Floridsdorf";
        String end = "AT, 1230 Vienna, Bahnhof Alterlaa";

        OpenRouteServiceClient openRouteServiceClient = new OpenRouteServiceClient();
        String directions = openRouteServiceClient.getTourInformation(start, end, ETransportType.Cycling);

        OpenRouteServiceClient.openTourMapInBrowser(directions);
    }
}