package org.tomasdavid.vehicleroutingproblem;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

public class VrpView extends View {

    private VrpPainter vp;

    private VehicleRoutingSolution actualSolution;

    public VrpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        vp = new VrpPainter(context.getResources());
    }

    public void setActualSolution(VehicleRoutingSolution actualSolution) {
        this.actualSolution = actualSolution;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.actualSolution != null) {
            vp.paint(canvas, this.actualSolution);
        }
    }
}
