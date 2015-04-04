package org.tomasdavid.vehicleroutingproblem;

import android.os.AsyncTask;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

public class VrpSolverTask extends AsyncTask<VehicleRoutingSolution, VehicleRoutingSolution, VehicleRoutingSolution> {

    public static final String SOLVER_CONFIG = "vehicleRoutingSolverConfig.xml";

    private VrpFragment fragment;

    public VrpSolverTask(VrpFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected VehicleRoutingSolution doInBackground(VehicleRoutingSolution... vrs) {
        Solver solver = SolverFactory.createFromXmlResource(SOLVER_CONFIG).buildSolver();
        solver.addEventListener(new SolverEventListener() {
            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent event) {
                publishProgress((VehicleRoutingSolution) event.getNewBestSolution());
            }
        });
        solver.solve(vrs[0]);
        return (VehicleRoutingSolution) solver.getBestSolution();
    }

    @Override
    protected void onProgressUpdate(VehicleRoutingSolution... solutions) {
        fragment.setVrs(solutions[0]);
    }

    @Override
    protected void onPostExecute(VehicleRoutingSolution solution) {
        fragment.setVrs(solution);
    }
}
