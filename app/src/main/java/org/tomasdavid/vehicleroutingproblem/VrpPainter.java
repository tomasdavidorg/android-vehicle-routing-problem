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

    private final int D = 6; //todo density

    private static final int TEXT_SIZE = 30;
    private static final int TIME_WINDOW_DIAMETER = 50;

    /**
     * Line colors list.
     */
    private static final int[] COLOR_SEQUENCE = {
            R.color.blue,
            R.color.red,
            R.color.green,
            R.color.orange,
            R.color.purple,
            R.color.yellow,
            R.color.brown
    };

    private static final int[] VEHICLES = {
            R.drawable.vehicle_blue,
            R.drawable.vehicle_red,
            R.drawable.vehicle_green,
            R.drawable.vehicle_orange,
            R.drawable.vehicle_purple,
            R.drawable.vehicle_yellow,
            R.drawable.vehicle_brown
    };

    /**
     * Customer, depot, line and text paint.
     */
    private Paint cp, dp, lp, tp;

    private void initPaint(Resources res) {
        cp = new Paint();
        cp.setColor(res.getColor(R.color.grey));

        dp = new Paint();
        dp.setColor(res.getColor(R.color.blue_grey));

        lp = new Paint();
        lp.setStyle(Style.STROKE);
        lp.setAntiAlias(true);
        lp.setStrokeWidth(3.0f);

        tp = new Paint();
        tp.setColor(res.getColor(R.color.black));
        tp.setTextSize(TEXT_SIZE);
    }

    public void paint(VehicleRoutingSolution vrs, Canvas c, Resources res) {
        initPaint(res);

        float width = c.getWidth();
        float height = c.getHeight();

        LatitudeLongitudeTranslator tlr = new LatitudeLongitudeTranslator(vrs, width, height);

        int maximumTimeWindowTime = determineMaximumTimeWindowTime(vrs);

//        g.setFont(g.getFont().deriveFont((float) TEXT_SIZE));
//        g.setStroke(TangoColorFactory.NORMAL_STROKE);

        // customer paint


        for (Customer customer : vrs.getCustomerList()) {
            Location location = customer.getLocation();
            int x = tlr.translateLongitudeToX(location.getLongitude());
            int y = tlr.translateLatitudeToY(location.getLatitude());
            c.drawRect(x - D, y - D, x + D, y + D, cp);

            String demandString = Integer.toString(customer.getDemand());
            c.drawText(demandString, x - (tp.measureText(demandString) / 2), y - TEXT_SIZE/2, tp);

            if (customer instanceof TimeWindowedCustomer) {
                TimeWindowedCustomer timeWindowedCustomer = (TimeWindowedCustomer) customer;
                cp.setColor(res.getColor(R.color.light_grey));
                int circleX = x - (TIME_WINDOW_DIAMETER / 2);
                int circleY = y + 5;
                cp.setStyle(Style.STROKE);
                c.drawOval(new RectF(circleX + D, circleY + D, circleX + D + TIME_WINDOW_DIAMETER, circleY + D + TIME_WINDOW_DIAMETER), cp);
                cp.setStyle(Style.FILL);
                c.drawArc(new RectF(circleX + D, circleY + D, circleX + D + TIME_WINDOW_DIAMETER, circleY + D + TIME_WINDOW_DIAMETER),
                        0 - calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getReadyTime()),
                        calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getReadyTime())
                                - calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getDueTime()), true, cp);

                //g.drawOval(circleX, circleY, TIME_WINDOW_DIAMETER, TIME_WINDOW_DIAMETER);
                //g.fillArc(circleX, circleY, TIME_WINDOW_DIAMETER, TIME_WINDOW_DIAMETER,
//                        90 - calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getReadyTime()),
//                        calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getReadyTime())
//                                - calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getDueTime()));
                if (timeWindowedCustomer.getArrivalTime() != null) {
                    if (timeWindowedCustomer.isArrivalAfterDueTime()) {
                        lp.setColor(res.getColor(R.color.dark_red));
//                        g.setColor(TangoColorFactory.SCARLET_2);
                    } else if (timeWindowedCustomer.isArrivalBeforeReadyTime()) {
                        lp.setColor(res.getColor(R.color.dark_orange));
//                        g.setColor(TangoColorFactory.ORANGE_2);
                    } else {
                        lp.setColor(res.getColor(R.color.dark_grey));
//                        g.setColor(TangoColorFactory.ALUMINIUM_6);
                    }
                    lp.setStrokeWidth(5.0f);
//                    g.setStroke(TangoColorFactory.THICK_STROKE);
                    int circleCenterY = y + 5 + TIME_WINDOW_DIAMETER / 2;
                    int angle = calculateTimeWindowDegree(maximumTimeWindowTime, timeWindowedCustomer.getArrivalTime());
                    c.drawLine(x + D, circleCenterY + D,
                            x + D + (int) (Math.sin(Math.toRadians(angle)) * (TIME_WINDOW_DIAMETER / 2 + 3)),
                            circleCenterY + D - (int) (Math.cos(Math.toRadians(angle)) * (TIME_WINDOW_DIAMETER / 2 + 3)), lp);
//                    g.drawLine(x, circleCenterY,
//                            x + (int) (Math.sin(Math.toRadians(angle)) * (TIME_WINDOW_DIAMETER / 2 + 3)),
//                            circleCenterY - (int) (Math.cos(Math.toRadians(angle)) * (TIME_WINDOW_DIAMETER / 2 + 3)));
//                    g.setStroke(TangoColorFactory.NORMAL_STROKE);

                    lp.setStrokeWidth(3.0f);
                    cp.setColor(res.getColor(R.color.grey));
                }
            }
        }


        for (Depot depot : vrs.getDepotList()) {
            int x = tlr.translateLongitudeToX(depot.getLocation().getLongitude());
            int y = tlr.translateLatitudeToY(depot.getLocation().getLatitude());
            c.drawRect(x - D, y - D, x + D, y + D, dp);
            Bitmap depotBitmap = BitmapFactory.decodeResource(res, R.drawable.depot);
            c.drawBitmap(depotBitmap, x - depotBitmap.getWidth() / 2, y - D - depotBitmap.getHeight(), null);
        }

        int colorIndex = 0;

        for (Vehicle vehicle : vrs.getVehicleList()) {
            lp.setColor(res.getColor(COLOR_SEQUENCE[colorIndex]));
            Customer vehicleInfoCustomer = null;
            int longestNonDepotDistance = -1;
            int load = 0;
            for (Customer customer : vrs.getCustomerList()) {
                if (customer.getPreviousStandstill() != null && customer.getVehicle() == vehicle) {
                    load += customer.getDemand();
                    Location previousLocation = customer.getPreviousStandstill().getLocation();
                    Location location = customer.getLocation();
                    drawRoute(c, tlr, previousLocation.getLongitude(), previousLocation.getLatitude(),
                            location.getLongitude(), location.getLatitude(),
                            location instanceof AirLocation);
                    // Determine where to draw the vehicle info
                    int distance = customer.getDistanceFromPreviousStandstill();
                    if (customer.getPreviousStandstill() instanceof Customer) {
                        if (longestNonDepotDistance < distance) {
                            longestNonDepotDistance = distance;
                            vehicleInfoCustomer = customer;
                        }
                    } else if (vehicleInfoCustomer == null) {
                        // If there is only 1 customer in this chain, draw it on a line to the Depot anyway
                        vehicleInfoCustomer = customer;
                    }
                    // Line back to the vehicle depot
                    if (customer.getNextCustomer() == null) {
                        Location vehicleLocation = vehicle.getLocation();
                        lp.setPathEffect(new DashPathEffect(new float[] {25.0f, 25.0f}, 0.0f));
                        drawRoute(c, tlr, location.getLongitude(), location.getLatitude(),
                                vehicleLocation.getLongitude(), vehicleLocation.getLatitude(),
                                location instanceof AirLocation);
                        lp.setPathEffect(null);
                    }
                }
            }

            if (vehicleInfoCustomer != null) {
                if (load > vehicle.getCapacity()) {
                    tp.setColor(res.getColor(R.color.dark_red));
                }
                Location previousLocation = vehicleInfoCustomer.getPreviousStandstill().getLocation();
                Location location = vehicleInfoCustomer.getLocation();
                double longitude = (previousLocation.getLongitude() + location.getLongitude()) / 2.0;
                int x = tlr.translateLongitudeToX(longitude);
                double latitude = (previousLocation.getLatitude() + location.getLatitude()) / 2.0;
                int y = tlr.translateLatitudeToY(latitude);
                boolean ascending = (previousLocation.getLongitude() < location.getLongitude())
                        ^ (previousLocation.getLatitude() < location.getLatitude());

                Bitmap vehicleBitmap = BitmapFactory.decodeResource(res, VEHICLES[colorIndex]);
                int vehicleInfoHeight = vehicleBitmap.getHeight() + 2 + TEXT_SIZE;

                c.drawBitmap(vehicleBitmap, x + 1, (ascending ? y - vehicleInfoHeight - 1 : y + 1), null);
                c.drawText(load + " / " + vehicle.getCapacity(), x + 1, (ascending ? y - 1 : y + vehicleInfoHeight + 1), tp);
                tp.setColor(res.getColor(R.color.black));
            }
            colorIndex = (colorIndex + 1) % COLOR_SEQUENCE.length;
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
