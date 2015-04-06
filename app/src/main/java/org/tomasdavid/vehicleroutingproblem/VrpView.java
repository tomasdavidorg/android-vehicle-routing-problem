package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

import java.util.ArrayList;
import java.util.List;

public class VrpView extends View {

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
            listAdapter.addAll(newStatisticItemList);
            listAdapter.notifyDataSetChanged();
        }
    }
}
