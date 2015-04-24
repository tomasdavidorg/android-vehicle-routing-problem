/*
 * Copyright 2015 Tomas David
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tomasdavid.vehicleroutingproblem.painter;

import android.content.res.Resources;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.tomasdavid.vehicleroutingproblem.R;

/**
 * Class for translation between longitude/latitude and screen coordinate system.
 * This file was modified from original file LatitudeLongitudeTranslator.java from OptaPlanner project.
 *
 * @author Tomas David
 * @author Geoffrey De Smet
 */
public class VrpTranslator {

    private double minLatitude = Double.MAX_VALUE;
    private double maxLatitude = -Double.MAX_VALUE;
    private double minLongitude = Double.MAX_VALUE;
    private double maxLongitude = -Double.MAX_VALUE;

    private double latitudeLength = 0.0;
    private double longitudeLength = 0.0;

    private double width = 0.0;
    private double height = 0.0;

    private double widthMargin = 0.0;
    private double heightMargin = 0.0;

    private double scale;

    /**
     * Constructor prepares vehicle routing solution for specified screen size.
     * @param vrs Vehicle routing solution.
     * @param width Width of screen.
     * @param height Height of screen.
     * @param resources Application resources.
     */
    public VrpTranslator(VehicleRoutingSolution vrs, double width, double height,
                         Resources resources) {
        for (Location location : vrs.getLocationList()) {
            addCoordinates(location.getLatitude(), location.getLongitude());
        }

        this.widthMargin = resources.getDimension(R.dimen.margin_width);
        this.heightMargin = resources.getDimension(R.dimen.margin_height);

        this.width = width - 2.0 * widthMargin;
        this.height = height - 2.0 * heightMargin;

        latitudeLength = maxLatitude - minLatitude;
        longitudeLength = maxLongitude - minLongitude;

        if (this.width / longitudeLength > this.height / latitudeLength) {
            scale =  this.height / latitudeLength;
        } else {
            scale = this.width / longitudeLength;
        }
    }

    /**
     * Add coordinates to find out min/max longitude/latitude.
     * @param latitude Point latitude.
     * @param longitude Point longitude.
     */
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

    /**
     * Converts point longitude to x coordinate.
     * @param longitude Point longitude.
     * @return X coordinate on screen.
     */
    public float translateLongitudeToX(double longitude) {
        return (float) Math.floor(((longitude - minLongitude) * scale) + widthMargin + (width-longitudeLength * scale) / 2.0);
    }

    /**
     * Converts point latitude to y coordinate.
     * @param latitude Point latitude.
     * @return Y coordinate on screen.
     */
    public float translateLatitudeToY(double latitude) {
        return (float) Math.floor(((maxLatitude - latitude) * scale) + heightMargin + (height-latitudeLength * scale) / 2.0);
    }
}
