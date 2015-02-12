package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

import java.util.List;

public class SolverTask extends AsyncTask<VehicleRoutingSolution, Context, String> {

    public static final String SOLVER_CONFIG
            = "vehicleRoutingSolverConfig.xml";

    private Activity activity;

    public SolverTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(VehicleRoutingSolution... vrs) {
        SolverFactory solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        Solver solver = solverFactory.buildSolver();

        solver.solve(vrs[0]);

        VehicleRoutingSolution bs = (VehicleRoutingSolution)solver.getBestSolution();

        String s = "RESULTS:\nBest score is: " + bs.getScore().toString() + "\n";

        List<Customer> cl = bs.getCustomerList();

        for (Customer c : cl) {
            s += "Customer: " + c.getId() + " is served by vehicle " + c.getVehicle().getId() + "\n";
        }
        return s;
    }

    protected void onPostExecute(String result) {
        TextView tv = (TextView)activity.findViewById(R.id.text_view);
        tv.setText(result);
    }
}
