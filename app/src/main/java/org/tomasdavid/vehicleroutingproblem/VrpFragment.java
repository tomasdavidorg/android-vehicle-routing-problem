package org.tomasdavid.vehicleroutingproblem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.persistence.VehicleRoutingImporter;

import java.io.IOException;

public class VrpFragment extends Fragment {

    private String fileName;

    public VrpFragment() {
    }

    public VrpFragment(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vrp, container, false);
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

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i("", "XXX " + metrics.density);

            VehicleRoutingSolution vrs = null;
            try {
                vrs = (VehicleRoutingSolution) VehicleRoutingImporter
                        .readSolution(fileName, getActivity().getAssets().open(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new SolverTask(getActivity()).execute(vrs);
            return true;
        } else if (id == R.id.action_about) {
            AboutAppDialog aad = new AboutAppDialog();
            aad.show(getActivity().getSupportFragmentManager(), "");
        } else if (id == R.id.action_legend) {
            LegendDialog aad = new LegendDialog();
            aad.show(getActivity().getSupportFragmentManager(), "");
        }

        return super.onOptionsItemSelected(item);
    }
}
