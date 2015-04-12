package org.tomasdavid.vehicleroutingproblem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import org.tomasdavid.vehicleroutingproblem.components.StopSolverDialog;
import org.tomasdavid.vehicleroutingproblem.fragments.MainFragment;
import org.tomasdavid.vehicleroutingproblem.fragments.VrpFragment;
import org.tomasdavid.vehicleroutingproblem.tasks.VrpSolverTask;

/**
 * Main and only activity of application.
 */
public class MainActivity extends ActionBarActivity {

    /**
     * Navigation drawer for displaying statistics.
     */
    private DrawerLayout statsDrawer;

    /**
     * Unlock navigation drawer.
     */
    public void unlockDrawer() {
        statsDrawer.setDrawerLockMode(DrawerLayout.VISIBLE);
    }

    /**
     * Hide and lock navigation drawer.
     */
    public void lockDrawer() {
        statsDrawer.closeDrawer(Gravity.START);
        statsDrawer.setDrawerLockMode(DrawerLayout.INVISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        statsDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lockDrawer();
        if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main, fragment).commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_main);
        if (fragment instanceof VrpFragment) {
            VrpSolverTask vrpSolverTask =  ((VrpFragment) fragment).getVrpSolverTask();
            if (vrpSolverTask.isRunning()) {
                new StopSolverDialog().show(getSupportFragmentManager(), null);
                return;
            } else {
                lockDrawer();
            }
        }
        super.onBackPressed();
    }
}
