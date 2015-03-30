/*
 * This file was modified from original file VehicleRoutingSolutionPainter.java from OptaPlanner project.
 */
package org.tomasdavid.vehicleroutingproblem;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.graphics.RectF;

import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedCustomer;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedDepot;

public class VrpPainter {

    /**
     * Application resources.
     */
    private Resources res;

    /**
     * Customer, depot, line, text and time-window paint.
     */
    private Paint cc, ci, ct, lp, wp;

    /**
     * Constructor for vehicle routing problem painter.
     * @param res Application resources.
     */
    public VrpPainter(Resources res) {
        this.res = res;

        // customer inside
        ci = new Paint();
        ci.setStyle(Style.FILL);
        ci.setColor(res.getColor(R.color.transparent_white));
        ci.setAntiAlias(true);

        // customer circle
        cc = new Paint();
        cc.setStyle(Style.STROKE);
        cc.setColor(res.getColor(R.color.black));
        cc.setAntiAlias(true);

        // customer text
        ct = new Paint();
        ct.setColor(res.getColor(R.color.black));
        ct.setTextSize(res.getDimension(R.dimen.customer_radius));
        ct.setAntiAlias(true);

        lp = new Paint();
        lp.setStyle(Style.STROKE);
        lp.setAntiAlias(true);
        lp.setStrokeWidth(3.0f);
        lp.setStrokeCap(Cap.ROUND);
        lp.setStrokeJoin(Join.ROUND);



        wp = new Paint();
        wp.setColor(res.getColor(R.color.light_grey));
        wp.setAntiAlias(true);
    }

    public void paint(Canvas c, VehicleRoutingSolution vrs) {
        int mtwt = determineMaximumTimeWindowTime(vrs);
        float width = c.getWidth();
        float height = c.getHeight();
        float lpr = res.getDimension(R.dimen.location_point_radius);
        float twd = res.getDimension(R.dimen.time_window_diameter);
        VrpTranslator llt = new VrpTranslator(vrs, width, height, res);

        int colorIndex = 0;

        for (Vehicle vehicle : vrs.getVehicleList()) {
            lp.setColor((res.obtainTypedArray(R.array.vehicle_colors).getColor(colorIndex, 0)));
            Customer vic = null;
            int longestNonDepotDistance = -1;
            int load = 0;
            for (Customer customer : vrs.getCustomerList()) {
                if (customer.getPreviousStandstill() != null && customer.getVehicle() == vehicle) {
                    load += customer.getDemand();
                    Location previousLocation = customer.getPreviousStandstill().getLocation();
                    Location location = customer.getLocation();
                    drawRoute(c, llt, previousLocation.getLongitude(),
                            previousLocation.getLatitude(), location.getLongitude(),
                            location.getLatitude());
                    int distance = customer.getDistanceFromPreviousStandstill();

                    if (customer.getPreviousStandstill() instanceof Customer) {
                        if (longestNonDepotDistance < distance) {
                            longestNonDepotDistance = distance;
                            vic = customer;
                        }
                    } else if (vic == null) {
                        vic = customer;
                    }

                    if (customer.getNextCustomer() == null) {
                        Location vehicleLocation = vehicle.getLocation();
                        lp.setPathEffect(new DashPathEffect(new float[] {25.0f, 25.0f}, 0.0f));
                        drawRoute(c, llt, location.getLongitude(), location.getLatitude(),
                                vehicleLocation.getLongitude(), vehicleLocation.getLatitude());
                        lp.setPathEffect(null);
                    }
                }
            }

            if (vic != null) {
                if (load > vehicle.getCapacity()) {
                    ct.setColor(res.getColor(R.color.dark_red));
                }
                Location prevLocation = vic.getPreviousStandstill().getLocation();
                Location location = vic.getLocation();
                double longitude = (prevLocation.getLongitude() + location.getLongitude()) / 2.0;
                float x = llt.translateLongitudeToX(longitude);
                double latitude = (prevLocation.getLatitude() + location.getLatitude()) / 2.0;
                float y = llt.translateLatitudeToY(latitude);
                boolean ascending = (prevLocation.getLongitude() < location.getLongitude())
                        ^ (prevLocation.getLatitude() < location.getLatitude());

                int resId = res.obtainTypedArray(R.array.vehicles).getResourceId(colorIndex, 0);
                Bitmap vehBitmap = BitmapFactory.decodeResource(res, resId);
                int vih = (int) (vehBitmap.getHeight() + 2 + res.getDimension(R.dimen.text_size));

                c.drawBitmap(vehBitmap, x + 1, (ascending ? y - vih - 1 : y + 1), null);
                c.drawText(load + " / " + vehicle.getCapacity(), x + 1,
                        (ascending ? y - 1 : y + vih + 1), ct);
                ct.setColor(res.getColor(R.color.black));
            }

            // change color index
            colorIndex = (colorIndex + 1) % res.obtainTypedArray(R.array.vehicle_colors).length();
        }

        // draw depots
        for (Depot depot : vrs.getDepotList()) {
            float x = llt.translateLongitudeToX(depot.getLocation().getLongitude());
            float y = llt.translateLatitudeToY(depot.getLocation().getLatitude());
            Bitmap depotBitmap = BitmapFactory.decodeResource(res, R.drawable.depot);
            c.drawBitmap(depotBitmap,
                    x - depotBitmap.getWidth() / 2,
                    y - depotBitmap.getHeight() / 2,
                    null);
        }

        // draw customers
        for (Customer customer : vrs.getCustomerList()) {
            Location location = customer.getLocation();
            float x = llt.translateLongitudeToX(location.getLongitude());
            float y = llt.translateLatitudeToY(location.getLatitude());
            float radius = res.getDimension(R.dimen.customer_radius);

            // draw customer circle
            RectF oval = new RectF(x - radius, y - radius, x + radius, y + radius);
            c.drawOval(oval, ci);
            c.drawOval(oval, cc);
            String demandString = Integer.toString(customer.getDemand());
            c.drawText(demandString,
                    x - ct.measureText(demandString) / 2,
                    y + res.getDimension(R.dimen.customer_radius) / 2,
                    ct);

            // draw customer time window
            if (customer instanceof TimeWindowedCustomer) {
                TimeWindowedCustomer twc = (TimeWindowedCustomer) customer;
                float circleX = (int) (x - (twd / 2));
                float circleY = y + 5;
                wp.setStyle(Style.STROKE);
                RectF tw = new RectF(circleX + lpr, circleY + lpr, circleX + lpr + twd,
                        circleY + lpr + twd);
                c.drawOval(tw, wp);
                wp.setStyle(Style.FILL);
                c.drawArc(tw, 0 - calculateTimeWindowDegree(mtwt, twc.getReadyTime()),
                        calculateTimeWindowDegree(mtwt, twc.getReadyTime())
                                - calculateTimeWindowDegree(mtwt, twc.getDueTime()), true, wp);

                if (twc.getArrivalTime() != null) {
                    if (twc.isArrivalAfterDueTime()) {
                        lp.setColor(res.getColor(R.color.dark_red));
                    } else if (twc.isArrivalBeforeReadyTime()) {
                        lp.setColor(res.getColor(R.color.dark_orange));
                    } else {
                        lp.setColor(res.getColor(R.color.dark_grey));
                    }
                    lp.setStrokeWidth(res.getDimension(R.dimen.stroke_thick));
                    int circleCenterY = (int) (y + 5 + twd / 2);
                    int angle = calculateTimeWindowDegree(mtwt, twc.getArrivalTime());
                    c.drawLine(x + lpr, circleCenterY + lpr,
                            x + lpr + (int) (Math.sin(Math.toRadians(angle)) * (twd / 2 + 3)),
                            circleCenterY + lpr - (int) (Math.cos(Math.toRadians(angle))
                                    * (twd / 2 + 3)), lp);
                    lp.setStrokeWidth(res.getDimension(R.dimen.stroke_normal));
                }
            }
        }
    }

    /**
     * Determines maximum time window time.
     * @param solution Vehicle routing solution.
     * @return Maximum time window time.
     */
    private int determineMaximumTimeWindowTime(VehicleRoutingSolution solution) {
        int maximumTimeWindowTime = 0;
        for (Depot depot : solution.getDepotList()) {
            if (depot instanceof TimeWindowedDepot) {
                int timeWindowTime = ((TimeWindowedDepot) depot).getDueTime();
                if (timeWindowTime > maximumTimeWindowTime) {
                    maximumTimeWindowTime = timeWindowTime;
                }
            }
        }
        for (Customer customer : solution.getCustomerList()) {
            if (customer instanceof TimeWindowedCustomer) {
                int timeWindowTime = ((TimeWindowedCustomer) customer).getDueTime();
                if (timeWindowTime > maximumTimeWindowTime) {
                    maximumTimeWindowTime = timeWindowTime;
                }
            }
        }
        return maximumTimeWindowTime;
    }

    /**
     * Calculates time window degree.
     * @param maximumTimeWindowTime Maximum time window time.
     * @param timeWindowTime Actual time window time.
     * @return Time window degree.
     */
    private int calculateTimeWindowDegree(int maximumTimeWindowTime, int timeWindowTime) {
        return (360 * timeWindowTime / maximumTimeWindowTime);
    }

    /**
     * Draws vehicle route to canvas.
     * @param canvas Drawing canvas.
     * @param translator Longitude/Latitude translator.
     * @param lon1 Start point longitude.
     * @param lat1 Start point latitude.
     * @param lon2 End point longitude.
     * @param lat2 End point latitude.
     */
    public void drawRoute(Canvas canvas, VrpTranslator translator, double lon1, double lat1,
                          double lon2, double lat2) {
        float x1 = translator.translateLongitudeToX(lon1);
        float y1 = translator.translateLatitudeToY(lat1);
        float x2 = translator.translateLongitudeToX(lon2);
        float y2 = translator.translateLatitudeToY(lat2);
        Path line = new Path();
        line.moveTo(x1, y1);
        line.lineTo(x2, y2);
        canvas.drawPath(line, lp);
    }
}
