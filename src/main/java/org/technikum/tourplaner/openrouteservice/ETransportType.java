package org.technikum.tourplaner.openrouteservice;

import lombok.Getter;

@Getter
public enum ETransportType {
    Driving("driving-car"),
    Cycling("cycling-regular"),
    Walking("foot-walking");

    private final String apiParameter;

    ETransportType(String apiParameter){
        this.apiParameter = apiParameter;
    }
}
