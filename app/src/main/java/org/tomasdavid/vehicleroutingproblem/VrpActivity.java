package org.tomasdavid.vehicleroutingproblem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.persistence.VehicleRoutingImporter;

import java.io.IOException;


public class VrpActivity extends ActionBarActivity {

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrp);
        fileName = getIntent().getExtras().getString("fileName");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vrp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_run) {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i("", "XXX " + metrics.density);

            VehicleRoutingSolution vrs = null;
            try {
                vrs = (VehicleRoutingSolution) VehicleRoutingImporter
                        .readSolution(fileName, getAssets().open(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new SolverTask(this).execute(vrs);
            return true;
        } else if (id == R.id.action_about) {
            AboutAppDialog aad = new AboutAppDialog();
            aad.show(getSupportFragmentManager(), "NoticeDialogFragment");
        }

        return super.onOptionsItemSelected(item);
    }
}
