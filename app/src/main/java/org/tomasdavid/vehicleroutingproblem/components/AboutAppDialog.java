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
        builder.setTitle(TITLE)
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
