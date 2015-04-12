package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.tomasdavid.vehicleroutingproblem.fragments.VrpFragment;

import java.io.IOException;
import java.io.InputStream;

public class VrpSolverTask extends AsyncTask<VehicleRoutingSolution, VehicleRoutingSolution, VehicleRoutingSolution> {

    private static final String TAG = "VrpSolverTask";

    private boolean running;

    private int timeLimit;

    private Solver solver;

    private VrpFragment fragment;

    private Toast toast;

    private String algorithm;

    public VrpSolverTask(VrpFragment fragment, int timeLimit, String algorithm) {
        this.fragment = fragment;
        this.running = false;
        this.timeLimit = timeLimit;
        this.algorithm = algorithm;
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

        try {
            InputStream is = fragment.getActivity().getAssets().open("solvers/" + algorithm);
            String solverConfig = IOUtils.toString(is);
            solver = SolverFactory.createFromXmlInputStream(IOUtils.toInputStream(solverConfig.replace("TIME_LIMIT", Integer.toString(timeLimit)))).buildSolver();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
