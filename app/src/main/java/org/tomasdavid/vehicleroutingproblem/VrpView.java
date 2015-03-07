package org.tomasdavid.vehicleroutingproblem;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

public class VrpView extends View {

    private VehicleRoutingSolution actualSolution;

    private Context context;

    public VrpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setActualSolution(VehicleRoutingSolution actualSolution) {
        this.actualSolution = actualSolution;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.actualSolution != null) {
            new VrpPainter(context.getResources(), canvas).paint(this.actualSolution);
        }
    }
}
