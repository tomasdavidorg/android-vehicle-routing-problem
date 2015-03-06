/*
 * This file was modified from original file LatitudeLongitudeTranslator.java from OptaPlanner project.
 */
package org.tomasdavid.vehicleroutingproblem;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;

//todo margin for various displays
public class LatitudeLongitudeTranslator {

    private double MARGIN = 50.0;
    private double minimumLatitude = Double.MAX_VALUE;
    private double maximumLatitude = -Double.MAX_VALUE;
    private double minimumLongitude = Double.MAX_VALUE;
    private double maximumLongitude = -Double.MAX_VALUE;
    private double latitudeLength = 0.0;
    private double longitudeLength = 0.0;
    private double width = 0.0;
    private double height = 0.0;

    public LatitudeLongitudeTranslator(VehicleRoutingSolution vrs, double width, double height) {
        for (Location location : vrs.getLocationList()) {
            addCoordinates(location.getLatitude(), location.getLongitude());
        }

        this.width = width - 2 * MARGIN;
        this.height = height - 2 * MARGIN;
        latitudeLength = maximumLatitude - minimumLatitude;
        longitudeLength = maximumLongitude - minimumLongitude;
    }

    public void addCoordinates(double latitude, double longitude) {
        if (latitude < minimumLatitude) {
            minimumLatitude = latitude;
        }
        if (latitude > maximumLatitude) {
            maximumLatitude = latitude;
        }
        if (longitude < minimumLongitude) {
            minimumLongitude = longitude;
        }
        if (longitude > maximumLongitude) {
            maximumLongitude = longitude;
        }
    }

    public int translateLongitudeToX(double longitude) {
        return (int) Math.floor(((longitude - minimumLongitude) * width / longitudeLength) + MARGIN);
    }

    public int translateLatitudeToY(double latitude) {
        return (int) Math.floor(((maximumLatitude - latitude) * height / latitudeLength) + MARGIN);
    }
}
