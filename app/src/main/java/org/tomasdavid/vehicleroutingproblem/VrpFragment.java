package org.tomasdavid.vehicleroutingproblem;

import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.persistence.VehicleRoutingImporter;

import java.io.IOException;

public class VrpFragment extends Fragment {
//TODO asynctask displays solution when solves, refactoring VrpFragment
    private static final String TAG = "VrpFragment";

    private VehicleRoutingSolution vrs;

    private AsyncTask<VehicleRoutingSolution, Context, String> solverTask;

    public VrpFragment() {
        super();
        this.vrs = null;
        this.solverTask = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        String fileName = getArguments().getString(VrpKeys.VRP_FILE_NAME.name());
        try {
            vrs = (VehicleRoutingSolution)  VehicleRoutingImporter.readSolution(
                    fileName, getActivity().getAssets().open(fileName));
        } catch (IOException e) {
            Log.e(TAG, "Problem with vrp file.", e);
            Toast.makeText(getActivity(), "File was not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vrp, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((VrpView)getActivity().findViewById(R.id.vrp_view)).setActualSolution(vrs);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vrp, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_run) {
            solverTask = new SolverTask(getActivity()).execute(vrs);
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
