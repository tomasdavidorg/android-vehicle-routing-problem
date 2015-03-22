package org.tomasdavid.vehicleroutingproblem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;


/**
 * Dialog for provide basic information about the application.
 * @author Tomas David
 */
public class AboutAppDialog extends AlertDialog implements DialogInterface.OnClickListener {

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

    protected AboutAppDialog(Context context) {
        super(context);
        setTitle(TITLE);
        setMessage(Html.fromHtml(MESSAGE));
        this.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
