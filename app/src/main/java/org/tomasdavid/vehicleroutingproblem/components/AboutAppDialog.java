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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import org.tomasdavid.vehicleroutingproblem.R;

/**
 * Dialog for providing basic information about the application.
 *
 * @author Tomas David
 */
public class AboutAppDialog extends DialogFragment implements DialogInterface.OnClickListener {

    /**
     * OK button text.
     */
    private static final String OK = "OK";

    /**
     * Title of dialog.
     */
    private static final String TITLE = "About application";

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.action_about)
               .setView(inflater.inflate(R.layout.dialog_info, null))
               .setPositiveButton(OK, this);
        return builder.create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
