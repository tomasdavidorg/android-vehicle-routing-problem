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

package org.tomasdavid.vehicleroutingproblem.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.adapters.StatisticItem;
import org.tomasdavid.vehicleroutingproblem.adapters.StatisticsListAdapter;
import org.tomasdavid.vehicleroutingproblem.painter.VrpPainter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * View for displaying actual vehicle routing solution.
 *
 * @author Tomas David
 */
public class VrpView extends View {

    /**
     * Number format of actual score.
     */
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0.00");

    /**
     * Title for Hard/Soft score.
     */
    private static final String SCORE_TITLE = "Hard/Soft Score ";

    /**
     * Title for total distance.
     */
    private static final String DISTANCE_TITLE = "Distance ";

    /**
     * Divide factor.
     */
    private static final double FACTOR = 1000.0;

    /**
     * Vrp painter for drawing actual solution on screen.
     */
    private VrpPainter vrpPainter;

    /**
     * Actual solution which is painted on screen.
     */
    private VehicleRoutingSolution actualSolution;

    /**
     * Current activity.
     */
    private Activity activity;

    public VrpView(Context context) {
        super(context);
        init(context);
    }

    public VrpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VrpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes components of this view.
     * @param context Context of view.
     */
    public void init(Context context) {
        this.activity = (Activity) context;
        this.vrpPainter = new VrpPainter(context.getResources());
        ((ListView) activity.findViewById(R.id.left_drawer)).setAdapter(
                new StatisticsListAdapter(context, new ArrayList<StatisticItem>()));
    }

    /**
     * Actualizes actual vrp solution.
     * @param actualSolution Actual vrp solution.
     */
    public void setActualSolution(VehicleRoutingSolution actualSolution) {
        this.actualSolution = actualSolution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.actualSolution != null) {

            // paint actual solution and get list of vehicles
            List<StatisticItem> newStatisticItemList = vrpPainter.paint(canvas, this.actualSolution);

            // get list adapter and clear it
            StatisticsListAdapter listAdapter;
            listAdapter = (StatisticsListAdapter) ((ListView) activity.findViewById(R.id.left_drawer)).getAdapter();
            listAdapter.clear();

            // get actual score and add score and distance items to adapter
            HardSoftScore score = actualSolution.getScore();
            StatisticItem scoreItem, distanceItem;
            if (score == null) {
                scoreItem = new StatisticItem(0, SCORE_TITLE, " - ");
                distanceItem = new StatisticItem(0, DISTANCE_TITLE, " - ");
            } else {
                scoreItem = new StatisticItem(
                        0,
                        SCORE_TITLE,
                        actualSolution.getScore().getHardScore() + "/" + actualSolution.getScore().getSoftScore());
                distanceItem = new StatisticItem(
                        0,
                        DISTANCE_TITLE,
                        NUMBER_FORMAT.format((-score.getSoftScore()) / FACTOR));
            }

            // add all new items to nav. drawer
            listAdapter.add(scoreItem);
            listAdapter.add(distanceItem);
            listAdapter.addAll(newStatisticItemList);
            listAdapter.notifyDataSetChanged();
        }
    }
}
