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
import org.optaplanner.examples.vehiclerouting.domain.location.AirLocation;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedCustomer;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedDepot;

// todo double buffering
public class VrpPainter {

    /**
     * Application resources.
     */
    private Resources res;

    /**
     * Canvas for painting.
     */
    private Canvas c;

    /**
     * Customer, depot, line, text and time-window paint.
     */
    private Paint cp, dp, lp, tp, wp;

    /**
     * Constructor for vehicle routing problem painter.
     * @param res Application resources.
     * @param c Canvas for painting.
     */
    public VrpPainter(Resources res, Canvas c) {
        this.res = res;
        this.c = c;

        cp = new Paint();
        cp.setColor(res.getColor(R.color.grey));
        cp.setAntiAlias(true);

        dp = new Paint();
        dp.setColor(res.getColor(R.color.blue_grey));
        dp.setAntiAlias(true);

        lp = new Paint();
        lp.setStyle(Style.STROKE);
        lp.setAntiAlias(true);
        lp.setStrokeWidth(3.0f);
        lp.setStrokeCap(Cap.ROUND);
        lp.setStrokeJoin(Join.ROUND);

        tp = new Paint();
        tp.setColor(res.getColor(R.color.black));
        tp.setTextSize(res.getDimension(R.dimen.text_size));
        tp.setAntiAlias(true);

        wp = new Paint();
        wp.setColor(res.getColor(R.color.light_grey));
        wp.setAntiAlias(true);
    }

    public void paint(VehicleRoutingSolution vrs) {
        int mtwt = determineMaximumTimeWindowTime(vrs);
        float width = c.getWidth();
        float height = c.getHeight();
        float lpr = res.getDimension(R.dimen.location_point_radius);
        float twd = res.getDimension(R.dimen.time_window_diameter);
        LatitudeLongitudeTranslator llt = new LatitudeLongitudeTranslator(vrs, width, height,
                res.getDimension(R.dimen.canvas_margin));

        for (Customer customer : vrs.getCustomerList()) {
            Location location = customer.getLocation();
            int x = llt.translateLongitudeToX(location.getLongitude());
            int y = llt.translateLatitudeToY(location.getLatitude());
            c.drawRect(x - lpr, y - lpr, x + lpr, y + lpr, cp);

            String demandString = Integer.toString(customer.getDemand());
            c.drawText(demandString, x - (tp.measureText(demandString) / 2),
                    y - res.getDimension(R.dimen.text_size) / 2, tp);

            if (customer instanceof TimeWindowedCustomer) {
                TimeWindowedCustomer twc = (TimeWindowedCustomer) customer;
                int circleX = (int) (x - (twd / 2));
                int circleY = y + 5;
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

        for (Depot depot : vrs.getDepotList()) {
            int x = llt.translateLongitudeToX(depot.getLocation().getLongitude());
            int y = llt.translateLatitudeToY(depot.getLocation().getLatitude());
            c.drawRect(x - lpr, y - lpr, x + lpr, y + lpr, dp);
            Bitmap depotBitmap = BitmapFactory.decodeResource(res, R.drawable.depot);
            c.drawBitmap(depotBitmap, x - depotBitmap.getWidth() / 2,
                    y - lpr - depotBitmap.getHeight(), null);
        }

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
                            location.getLatitude(), location instanceof AirLocation);
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
                                vehicleLocation.getLongitude(), vehicleLocation.getLatitude(),
                                location instanceof AirLocation);
                        lp.setPathEffect(null);
                    }
                }
            }

            if (vic != null) {
                if (load > vehicle.getCapacity()) {
                    tp.setColor(res.getColor(R.color.dark_red));
                }
                Location prevLocation = vic.getPreviousStandstill().getLocation();
                Location location = vic.getLocation();
                double longitude = (prevLocation.getLongitude() + location.getLongitude()) / 2.0;
                int x = llt.translateLongitudeToX(longitude);
                double latitude = (prevLocation.getLatitude() + location.getLatitude()) / 2.0;
                int y = llt.translateLatitudeToY(latitude);
                boolean ascending = (prevLocation.getLongitude() < location.getLongitude())
                        ^ (prevLocation.getLatitude() < location.getLatitude());

                int resId = res.obtainTypedArray(R.array.vehicles).getResourceId(colorIndex, 0);
                Bitmap vehBitmap = BitmapFactory.decodeResource(res, resId);
                int vih = (int) (vehBitmap.getHeight() + 2 + res.getDimension(R.dimen.text_size));

                c.drawBitmap(vehBitmap, x + 1, (ascending ? y - vih - 1 : y + 1), null);
                c.drawText(load + " / " + vehicle.getCapacity(), x + 1,
                        (ascending ? y - 1 : y + vih + 1), tp);
                tp.setColor(res.getColor(R.color.black));
            }

            colorIndex = (colorIndex + 1) % res.obtainTypedArray(R.array.vehicle_colors).length();
        }
//
//        // Legend
//        g.setColor(TangoColorFactory.ALUMINIUM_3);
//        g.fillRect(5, (int) height - 12 - TEXT_SIZE - (TEXT_SIZE / 2), 5, 5);
//        g.drawString("Depot", 15, (int) height - 10 - TEXT_SIZE);
//        String vehiclesSizeString = solution.getVehicleList().size() + " vehicles";
//        g.drawString(vehiclesSizeString,
//                ((int) width - g.getFontMetrics().stringWidth(vehiclesSizeString)) / 2, (int) height - 10 - TEXT_SIZE);
//        g.setColor(TangoColorFactory.ALUMINIUM_4);
//        g.fillRect(6, (int) height - 6 - (TEXT_SIZE / 2), 3, 3);
//        g.drawString((solution instanceof TimeWindowedVehicleRoutingSolution)
//                ? "Customer: demand, time window and arrival time" : "Customer: demand", 15, (int) height - 5);
//        String customersSizeString = solution.getCustomerList().size() + " customers";
//        g.drawString(customersSizeString,
//                ((int) width - g.getFontMetrics().stringWidth(customersSizeString)) / 2, (int) height - 5);
//        if (solution.getDistanceType() == DistanceType.AIR_DISTANCE) {
//            String clickString = "Click anywhere in the map to add a customer.";
//            g.drawString(clickString, (int) width - 5 - g.getFontMetrics().stringWidth(clickString), (int) height - 5);
//        }
//        // Show soft score
//        g.setColor(TangoColorFactory.ORANGE_3);
//        HardSoftScore score = solution.getScore();
//        if (score != null) {
//            String distanceString;
//            if (!score.isFeasible()) {
//                distanceString = "Not feasible";
//            } else {
//                double distance = ((double) - score.getSoftScore()) / 1000.0;
//                distanceString = NUMBER_FORMAT.format(distance) + " " + solution.getDistanceUnitOfMeasurement();
//            }
//            g.setFont(g.getFont().deriveFont(Font.BOLD, (float) TEXT_SIZE * 2));
//            g.drawString(distanceString,
//                    (int) width - g.getFontMetrics().stringWidth(distanceString) - 10, (int) height - 10 - TEXT_SIZE);
//        }

    }

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

    private int calculateTimeWindowDegree(int maximumTimeWindowTime, int timeWindowTime) {
        return (360 * timeWindowTime / maximumTimeWindowTime);
    }

    public void drawRoute(Canvas c, LatitudeLongitudeTranslator translator, double lon1, double lat1,
                          double lon2, double lat2, boolean straight) {
        int x1 = translator.translateLongitudeToX(lon1);
        int y1 = translator.translateLatitudeToY(lat1);
        int x2 = translator.translateLongitudeToX(lon2);
        int y2 = translator.translateLatitudeToY(lat2);
        if (straight) {
            Path line = new Path();
            line.moveTo(x1, y1);
            line.lineTo(x2, y2);
            c.drawPath(line, lp);
            //TODO curve
//        } else {
//            double xDistPart = (x2 - x1) / 3.0;
//            double yDistPart = (y2 - y1) / 3.0;
//            double ctrlx1 = x1 + xDistPart + yDistPart;
//            double ctrly1 = y1 - xDistPart + yDistPart;
//            double ctrlx2 = x2 - xDistPart - yDistPart;
//            double ctrly2 = y2 + xDistPart - yDistPart;
//            g.draw(new CubicCurve2D.Double(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
        }
    }
}
