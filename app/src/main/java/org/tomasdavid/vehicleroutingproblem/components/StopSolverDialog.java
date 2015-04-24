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

package org.tomasdavid.vehicleroutingproblem.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import org.tomasdavid.vehicleroutingproblem.MainActivity;
import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.fragments.VrpFragment;

/**
 * Stop solver dialog. Displays when solver running and user clicks on the back button.
 *
 * @author Tomas David
 */
public class StopSolverDialog extends DialogFragment implements OnClickListener {

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.solver_running_message)
                .setPositiveButton(R.string.button_ok, this)
                .setNegativeButton(R.string.button_cancel, this);
        return builder.create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {

            // solver is stopped, nav. drawer is hidden and list fragment is displayed
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.activity_main);
            if (fragment instanceof VrpFragment) {
                ((Toolbar) getActivity().findViewById(R.id.toolbar)).setNavigationIcon(null);
                (((VrpFragment) fragment).getVrpSolverTask()).stopTask();
                ((MainActivity) getActivity()).lockDrawer();
                getActivity().onBackPressed();
            }
        }
    }
}
