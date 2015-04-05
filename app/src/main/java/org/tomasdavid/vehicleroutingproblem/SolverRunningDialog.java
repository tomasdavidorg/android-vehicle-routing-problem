package org.tomasdavid.vehicleroutingproblem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public class SolverRunningDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.solver_running_message)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.activity_main);
                        if (fragment instanceof VrpFragment) {
                            VrpSolverTask vrpSolverTask = ((VrpFragment) fragment).getVrpSolverTask();
                            vrpSolverTask.stopTask();
                            getActivity().onBackPressed();
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing happens
                    }
                });
        return builder.create();
    }
}
