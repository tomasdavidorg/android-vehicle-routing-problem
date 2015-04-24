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

package org.tomasdavid.vehicleroutingproblem.tasks;

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
import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.fragments.VrpFragment;

import java.io.IOException;
import java.io.InputStream;

/**
 * Vrp solver tasks for calculation of vehicle routing problem.
 *
 * @author Tomas David
 */
public class VrpSolverTask extends AsyncTask<VehicleRoutingSolution, VehicleRoutingSolution, VehicleRoutingSolution> {

    /**
     * Class tag.
     */
    private static final String TAG = "VrpSolverTask";

    /**
     * Status of this task.
     */
    private boolean running;

    /**
     * Time limit for solving.
     */
    private long timeLimit;

    /**
     * Solver of problem.
     */
    private Solver solver;

    /**
     * Fragment where solution is displayed.
     */
    private VrpFragment fragment;

    /**
     * Informative toast.
     */
    private Toast toast;

    /**
     * Algorithm for calculation.
     */
    private String algorithm;

    /**
     * Constructor of solver task.
     * @param fragment Fragment where solution is displayed.
     * @param timeLimit Time limit for solving.
     * @param algorithm Algorithm for calculation.
     */
    public VrpSolverTask(VrpFragment fragment, int timeLimit, String algorithm) {
        this.fragment = fragment;
        this.running = false;
        this.timeLimit = timeLimit;
        this.algorithm = algorithm;
    }

    /**
     * Returns true if solver is running otherwise false.
     * @return True if solver is running otherwise false.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Stops task and solver
     */
    public void stopTask() {
        running = false;
        if (solver != null) {
            solver.terminateEarly();
        }
    }

    /**
     * Cancel last toast.
     */
    public void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        running = true;
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(fragment.getActivity(), R.string.calculation_started, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected VehicleRoutingSolution doInBackground(VehicleRoutingSolution... vrs) {
        Log.d(TAG, "Building solver.");

        // creates solver
        try {
            InputStream is = fragment.getActivity().getAssets().open("solvers/" + algorithm);
            SolverFactory solverFactory = SolverFactory.createFromXmlInputStream(is);
            solverFactory.getSolverConfig().getTerminationConfig().setSecondsSpentLimit(timeLimit);
            solver = solverFactory.buildSolver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // adds solver listener for new best solution
        solver.addEventListener(new SolverEventListener() {
            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent event) {
                if (running) {
                    publishProgress((VehicleRoutingSolution) event.getNewBestSolution());
                }
            }
        });

        // starts solver
        Log.d(TAG, "Solver built, running solver.");
        solver.solve(vrs[0]);

        // end of calculation
        running = false;
        return (VehicleRoutingSolution) solver.getBestSolution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onProgressUpdate(VehicleRoutingSolution... solutions) {
        Log.d(TAG, "New best solution found.");
        fragment.setVrs(solutions[0]);
    }

    /**
     * {@inheritDoc}
     */
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
