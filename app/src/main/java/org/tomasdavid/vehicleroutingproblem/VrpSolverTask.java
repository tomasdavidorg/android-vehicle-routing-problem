package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Log;
import android.widget.Toast;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

public class VrpSolverTask extends AsyncTask<VehicleRoutingSolution, VehicleRoutingSolution, VehicleRoutingSolution> {

    private static final String TAG = "VrpSolverTask";

    public static final String SOLVER_CONFIG = "vehicleRoutingSolverConfig.xml";

    private boolean running;

    private Solver solver;

    private VrpFragment fragment;

    private Toast toast;

    public VrpSolverTask(VrpFragment fragment) {
        this.fragment = fragment;
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void stopTask() {
        running = false;
        if (solver != null) {
            solver.terminateEarly();
        }
    }

    public void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    protected void onPreExecute() {
        running = true;
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(fragment.getActivity(), R.string.calculation_started, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected VehicleRoutingSolution doInBackground(VehicleRoutingSolution... vrs) {
        Log.d(TAG, "Building solver.");
        solver = SolverFactory.createFromXmlResource(SOLVER_CONFIG).buildSolver();
        solver.addEventListener(new SolverEventListener() {
            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent event) {
                if (running) {
                    publishProgress((VehicleRoutingSolution) event.getNewBestSolution());
                }
            }
        });
        Log.d(TAG, "Solver built, running solver.");
        solver.solve(vrs[0]);
        running = false;
        return (VehicleRoutingSolution) solver.getBestSolution();
    }

    @Override
    protected void onProgressUpdate(VehicleRoutingSolution... solutions) {
        Log.d(TAG, "New best solution found.");
        fragment.setVrs(solutions[0]);
    }

    @Override
    protected void onPostExecute(VehicleRoutingSolution solution) {
        Log.d(TAG, "Calculation finished.");
        if (fragment != null) {
            Activity activity = fragment.getActivity();
            if (activity != null) {
                ActionMenuItemView item = (ActionMenuItemView) activity.findViewById(R.id.action_run);
                item.setIcon(activity.getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(activity, R.string.calculation_finished, Toast.LENGTH_SHORT);
                toast.show();
                fragment.setVrs(solution);
            }
        }
    }
}
