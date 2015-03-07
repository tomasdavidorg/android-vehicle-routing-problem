/*
 * This file was modified from original file LatitudeLongitudeTranslator.java from OptaPlanner project.
 */
package org.tomasdavid.vehicleroutingproblem;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;

//todo margin for various displays
public class LatitudeLongitudeTranslator {

    private float margin = 0.0f;
    private double minLatitude = Double.MAX_VALUE;
    private double maxLatitude = -Double.MAX_VALUE;
    private double minLongitude = Double.MAX_VALUE;
    private double maxLongitude = -Double.MAX_VALUE;
    private double latitudeLength = 0.0;
    private double longitudeLength = 0.0;
    private double width = 0.0;
    private double height = 0.0;

    public LatitudeLongitudeTranslator(VehicleRoutingSolution vrs, double width, double height, float margin) {
        for (Location location : vrs.getLocationList()) {
            addCoordinates(location.getLatitude(), location.getLongitude());
        }

        this.width = width - 2 * margin;
        this.height = height - 2 * margin;
        this.margin= margin;
        latitudeLength = maxLatitude - minLatitude;
        longitudeLength = maxLongitude - minLongitude;
    }

    public void addCoordinates(double latitude, double longitude) {
        if (latitude < minLatitude) {
            minLatitude = latitude;
        }
        if (latitude > maxLatitude) {
            maxLatitude = latitude;
        }
        if (longitude < minLongitude) {
            minLongitude = longitude;
        }
        if (longitude > maxLongitude) {
            maxLongitude = longitude;
        }
    }

    public int translateLongitudeToX(double longitude) {
        return (int) Math.floor(((longitude - minLongitude) * width / longitudeLength) + margin);
    }

    public int translateLatitudeToY(double latitude) {
        return (int) Math.floor(((maxLatitude - latitude) * height / latitudeLength) + margin);
    }
}
