package org.tomasdavid.vehicleroutingproblem;

import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main);
        if (fragment instanceof VrpFragment) {
            VrpSolverTask vrpSolverTask =  ((VrpFragment) fragment).getVrpSolverTask();
            if (vrpSolverTask.getStatus() == Status.RUNNING && !(vrpSolverTask.isCancelled())) {
                new SolverRunningDialog().show(getSupportFragmentManager(), null);
                return;
            }
        }
        super.onBackPressed();
    }
}
