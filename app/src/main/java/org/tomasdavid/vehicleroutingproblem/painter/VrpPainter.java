package org.tomasdavid.vehicleroutingproblem.painter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;

import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedCustomer;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedDepot;
import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.adapters.StatisticItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for painting vehicle routing solution to screen.
 * This file was modified from original file VehicleRoutingSolutionPainter.java from OptaPlanner project.
 *
 * @author Tomas David
 * @author Geoffrey De Smet
 */
public class VrpPainter {

    /**
     * Application resources.
     */
    private Resources res;

    /**
     * Customer, route and time window paints.
     */
    private Paint ci, cc, ct, rl, wp, wa, ww;

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
        ct.setStyle(Style.FILL_AND_STROKE);
        ct.setColor(res.getColor(R.color.black));
        ct.setAntiAlias(true);
        ct.setTextSize(res.getDimension(R.dimen.customer_radius));

        // route line
        rl = new Paint();
        rl.setStyle(Style.STROKE);
        rl.setAntiAlias(true);
        rl.setStrokeCap(Cap.ROUND);
        rl.setStrokeJoin(Join.ROUND);
        rl.setStrokeWidth(res.getDimension(R.dimen.stroke_normal));

        // time window pointer
        wp = new Paint();
        wp.setStyle(Style.STROKE);
        wp.setAntiAlias(true);
        wp.setStrokeCap(Cap.ROUND);
        wp.setStrokeJoin(Join.ROUND);
        wp.setStrokeWidth(res.getDimension(R.dimen.stroke_thick));

        // time window arc
        wa = new Paint();
        wa.setStyle(Style.STROKE);
        wa.setColor(res.getColor(R.color.black));
        wa.setAntiAlias(true);
        wa.setStrokeWidth(res.getDimension(R.dimen.stroke_thick));

        // time window arc wedge
        ww = new Paint();
        ww.setStyle(Style.FILL);
        ww.setColor(res.getColor(R.color.transparent_grey));
        ww.setAntiAlias(true);
    }

    /**
     * Paints vehicle routing solution to canvas.
     * @param c Canvas to paint.
     * @param vrs Vehicle routing solution.
     * @return List of statistic items for navigation drawer.
     */
    public List<StatisticItem> paint(Canvas c, VehicleRoutingSolution vrs) {
        int mtwt = determineMaximumTimeWindowTime(vrs);
        float width = c.getWidth();
        float height = c.getHeight();

        VrpTranslator vrpt = new VrpTranslator(vrs, width, height, res);

        List<StatisticItem> statisticItemList = new ArrayList<>();

        int colorIndex = 0;

        // draw routes
        for (Vehicle vehicle : vrs.getVehicleList()) {
            rl.setColor((res.obtainTypedArray(R.array.vehicle_colors).getColor(colorIndex, 0)));
            Customer vehicleInfoCustomer = null;
            int longestNonDepotDistance = -1;
            int load = 0;
            for (Customer customer : vrs.getCustomerList()) {
                if (customer.getPreviousStandstill() != null && customer.getVehicle() == vehicle) {
                    load += customer.getDemand();
                    Location previousLocation = customer.getPreviousStandstill().getLocation();
                    Location location = customer.getLocation();
                    drawRoute(c, vrpt, previousLocation.getLongitude(),
                            previousLocation.getLatitude(), location.getLongitude(),
                            location.getLatitude());
                    int distance = customer.getDistanceFromPreviousStandstill();

                    if (customer.getPreviousStandstill() instanceof Customer) {
                        if (longestNonDepotDistance < distance) {
                            longestNonDepotDistance = distance;
                            vehicleInfoCustomer = customer;
                        }
                    } else if (vehicleInfoCustomer == null) {
                        vehicleInfoCustomer = customer;
                    }

                    // draw route back to depot
                    if (customer.getNextCustomer() == null) {
                        Location vehicleLocation = vehicle.getLocation();
                        float dashLength = res.getDimension(R.dimen.dash_length);
                        rl.setPathEffect(new DashPathEffect(new float[] {dashLength, dashLength}, 0.0f));
                        drawRoute(c, vrpt, location.getLongitude(), location.getLatitude(),
                                vehicleLocation.getLongitude(), vehicleLocation.getLatitude());
                        rl.setPathEffect(null);
                    }
                }
            }

            if (vehicleInfoCustomer != null) {
//                if (load > vehicle.getCapacity()) {
//                    ct.setColor(res.getColor(R.color.dark_red));
//                }

                Location prevLocation = vehicleInfoCustomer.getPreviousStandstill().getLocation();
                Location location = vehicleInfoCustomer.getLocation();
                double longitude = (prevLocation.getLongitude() + location.getLongitude()) / 2.0;
                float x = vrpt.translateLongitudeToX(longitude);
                double latitude = (prevLocation.getLatitude() + location.getLatitude()) / 2.0;
                float y = vrpt.translateLatitudeToY(latitude);
//                boolean ascending = (prevLocation.getLongitude() < location.getLongitude())
//                        ^ (prevLocation.getLatitude() < location.getLatitude());

                int resId = res.obtainTypedArray(R.array.vehicles).getResourceId(colorIndex, 0);
                Bitmap vehBitmap = BitmapFactory.decodeResource(res, resId);
//                int vehicleInfoHeight = (int) (vehBitmap.getHeight() + res.getDimension(R.dimen.customer_radius));

                // draw vehicle and its capacity
                c.drawBitmap(vehBitmap, x, y, null);
//                c.drawText(load + " / " + vehicle.getCapacity(), x,
//                        (ascending ? y : y + vehicleInfoHeight), ct);
//                ct.setColor(res.getColor(R.color.black));
            }

            statisticItemList.add(new StatisticItem(
                    res.obtainTypedArray(R.array.vehicles).getResourceId(colorIndex, 0),
                    Vehicle.class.getSimpleName() + " " + vehicle.getId(),
                    load + " / " + vehicle.getCapacity()));

            // change color index
            colorIndex = (colorIndex + 1) % res.obtainTypedArray(R.array.vehicle_colors).length();
        }

        // draw depots
        for (Depot depot : vrs.getDepotList()) {
            float x = vrpt.translateLongitudeToX(depot.getLocation().getLongitude());
            float y = vrpt.translateLatitudeToY(depot.getLocation().getLatitude());
            Bitmap depotBitmap = BitmapFactory.decodeResource(res, R.drawable.depot);
            c.drawBitmap(depotBitmap,
                    x - depotBitmap.getWidth() / 2,
                    y - depotBitmap.getHeight() / 2,
                    null);
        }

        // draw customers
        for (Customer customer : vrs.getCustomerList()) {
            Location location = customer.getLocation();
            float x = vrpt.translateLongitudeToX(location.getLongitude());
            float y = vrpt.translateLatitudeToY(location.getLatitude());
            float radius = res.getDimension(R.dimen.customer_radius);

            // draw customer circle
            RectF oval = new RectF(x - radius, y - radius, x + radius, y + radius);
            c.drawOval(oval, ci);
            c.drawOval(oval, cc);
            String demandString = Integer.toString(customer.getDemand());

            // draw customer time window
            if (customer instanceof TimeWindowedCustomer) {
                // draw time window arc
                TimeWindowedCustomer twc = (TimeWindowedCustomer) customer;
                int startAngle = calculateTimeWindowDegree(mtwt, twc.getReadyTime()) - 90;
                int endAngle = calculateTimeWindowDegree(mtwt, twc.getDueTime())
                        - calculateTimeWindowDegree(mtwt, twc.getReadyTime());

                c.drawArc(oval, startAngle, endAngle, true, ww);
                c.drawArc(oval, startAngle, endAngle, false, wa);

                // draw time window pointer
                if (twc.getArrivalTime() != null) {
                    if (twc.isArrivalAfterDueTime()) {
                        wp.setColor(res.getColor(R.color.dark_red));
                    } else if (twc.isArrivalBeforeReadyTime()) {
                        wp.setColor(res.getColor(R.color.dark_orange));
                    } else {
                        wp.setColor(res.getColor(R.color.dark_green));
                    }

                    int angle = calculateTimeWindowDegree(mtwt, twc.getArrivalTime());
                    float xx = (float) Math.sin(Math.toRadians(angle));
                    float yy = (float) Math.cos(Math.toRadians(angle));

                    c.drawLine(x + xx * res.getDimension(R.dimen.time_window_pointer_start),
                            y - yy * res.getDimension(R.dimen.time_window_pointer_start),
                            x + xx * res.getDimension(R.dimen.time_window_pointer_end),
                            y - yy * res.getDimension(R.dimen.time_window_pointer_end),
                            wp);
                }
            }

            // draw customer text
            c.drawText(demandString,
                    x - ct.measureText(demandString) / 2,
                    y + res.getDimension(R.dimen.customer_radius) / 2,
                    ct);
        }

        return statisticItemList;
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
        canvas.drawPath(line, rl);
    }
}
