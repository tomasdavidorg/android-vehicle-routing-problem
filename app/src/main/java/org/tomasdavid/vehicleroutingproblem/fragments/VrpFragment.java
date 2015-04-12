package org.tomasdavid.vehicleroutingproblem.fragments;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.persistence.VehicleRoutingImporter;
import org.tomasdavid.vehicleroutingproblem.components.AboutAppDialog;
import org.tomasdavid.vehicleroutingproblem.components.LegendDialog;
import org.tomasdavid.vehicleroutingproblem.MainActivity;
import org.tomasdavid.vehicleroutingproblem.ProgressBarTask;
import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.VrpKeys;
import org.tomasdavid.vehicleroutingproblem.VrpSolverTask;
import org.tomasdavid.vehicleroutingproblem.components.VrpView;

import java.io.File;
import java.io.IOException;

public class VrpFragment extends Fragment {

    private static final String TAG = "VrpFragment";

    private VehicleRoutingSolution vrs;

    private VrpSolverTask vrpSolverTask;

    private ProgressBarTask progressBarTask;

    private int timeLimitInSeconds;

    private String algorithm;

    public VrpFragment() {
        super();
        this.vrs = null;
        this.vrpSolverTask = null;
        this.timeLimitInSeconds = 0;
        this.progressBarTask = null;
        this.algorithm = null;
    }

    public void setVrs(VehicleRoutingSolution vrs) {
        this.vrs = vrs;
        VrpView view = (VrpView) getActivity().findViewById(R.id.vrp_view);
        view.setActualSolution(vrs);
        view.invalidate();

    }

    public VrpSolverTask getVrpSolverTask() {
        return vrpSolverTask;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        String fileName = getArguments().getString(VrpKeys.VRP_FILE_NAME.name());
        try {
            vrs = (VehicleRoutingSolution)  VehicleRoutingImporter.readSolution(
                    fileName, getActivity().getAssets().open(getActivity().getString(R.string.vrps_dir) + File.separator + fileName));
        } catch (IOException e) {
            Log.e(TAG, "Problem with vrp file.", e);
            Toast.makeText(getActivity(), "File was not found.", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }

        timeLimitInSeconds = getArguments().getInt(VrpKeys.VRP_TIME_LIMIT.name());
        algorithm = getArguments().getString(VrpKeys.VRP_ALGORITHM.name());
        vrpSolverTask = new VrpSolverTask(this, timeLimitInSeconds, algorithm);
        progressBarTask = new ProgressBarTask(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vrp, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        ((VrpView) mainActivity.findViewById(R.id.vrp_view)).setActualSolution(vrs);
        mainActivity.unlockDrawer();

        ProgressBar pb = (ProgressBar)getActivity().findViewById(R.id.progress_bar);
        pb.setMax(timeLimitInSeconds);
        pb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vrp, menu);
        MenuItem playStopButton = menu.findItem(R.id.action_run);
        if (vrpSolverTask.isRunning()) {
            playStopButton.setIcon(R.drawable.ic_stop_white_24dp);
        } else {
            playStopButton.setIcon(R.drawable.ic_play_arrow_white_24dp);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_run) {
            vrpSolverTask.cancelToast();
            if (vrpSolverTask.isRunning()) {
                vrpSolverTask.stopTask();
                item.setIcon(R.drawable.ic_play_arrow_white_24dp);
            } else {
                item.setIcon(R.drawable.ic_stop_white_24dp);
                if (vrpSolverTask.getStatus() != Status.PENDING) {
                    vrpSolverTask = new VrpSolverTask(this, timeLimitInSeconds, algorithm);
                }
                vrpSolverTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, vrs);
                if (progressBarTask.getStatus() != Status.PENDING) {
                    progressBarTask = new ProgressBarTask(this);
                }
                progressBarTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeLimitInSeconds);
            }
            return true;
        } else if (id == R.id.action_about) {
            AboutAppDialog aad = new AboutAppDialog();
            aad.show(getActivity().getSupportFragmentManager(), "");
            return true;
        } else if (id == R.id.action_legend) {
            LegendDialog aad = new LegendDialog();
            aad.show(getActivity().getSupportFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
