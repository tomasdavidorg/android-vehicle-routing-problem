package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

public class SolverTask extends AsyncTask<VehicleRoutingSolution, Context, String> {

    public static final String SOLVER_CONFIG
            = "vehicleRoutingSolverConfig.xml";

    private Activity activity;

    public SolverTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(VehicleRoutingSolution... vrs) {
        VrpView vv = (VrpView)activity.findViewById(R.id.vrp_view);
        vv.setActualSolution(vrs[0]);
        vv.postInvalidate();
        SolverFactory solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        Solver solver = solverFactory.buildSolver();

        solver.solve(vrs[0]);

        VehicleRoutingSolution bs = (VehicleRoutingSolution)solver.getBestSolution();
        vv.setActualSolution(bs);
        vv.postInvalidate();

        return null;
    }

    protected void onPostExecute(String result) {
//        TextView tv = (TextView)activity.findViewById(R.id.text_view);
//        tv.setText(result);
    }
}
