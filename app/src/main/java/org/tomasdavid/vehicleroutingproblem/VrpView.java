package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class VrpView extends View {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0.00");

    private static final String SCORE_TITLE = "Hard/Soft Score ";

    private static final String DISTANCE_TITLE = "Distance ";

    private VrpPainter vp;

    private VehicleRoutingSolution actualSolution;

    private Activity context;

    List<StatisticItem> statisticItemList;

    public VrpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (Activity) context;
        vp = new VrpPainter(context.getResources());

        statisticItemList = new ArrayList<>();
        StatisticsListAdapter statisticsListAdapter = new StatisticsListAdapter(context, statisticItemList);
        ((ListView)((Activity) context).findViewById(R.id.left_drawer)).setAdapter(statisticsListAdapter);
    }

    public void setActualSolution(VehicleRoutingSolution actualSolution) {
        this.actualSolution = actualSolution;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.actualSolution != null) {
            List<StatisticItem> newStatisticItemList = vp.paint(canvas, this.actualSolution);

            ListView listView = (ListView) context.findViewById(R.id.left_drawer);
            StatisticsListAdapter listAdapter = (StatisticsListAdapter) listView.getAdapter();
            listAdapter.clear();
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
                        NUMBER_FORMAT.format(((double) - score.getSoftScore()) / 1000.0));
            }
            listAdapter.add(scoreItem);
            listAdapter.add(distanceItem);
            listAdapter.addAll(newStatisticItemList);
            listAdapter.notifyDataSetChanged();
        }
    }
}
