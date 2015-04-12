package org.tomasdavid.vehicleroutingproblem.components;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;

/**
* Dialog for providing basic information about the application.
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
     * Body of dialog.
     */
    private static final String MESSAGE = "<b>Author:</b> Tomas David<br>" +
            "<b>Year:</b> 2015" +
            "<p>Application demonstrate OptaPlanner functionality on the Android platform. " +
            "Demonstration is exemplified by Vehicle routing problem example.</p>";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(TITLE)
               .setMessage(Html.fromHtml(MESSAGE))
               .setPositiveButton(OK, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
