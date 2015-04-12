package org.tomasdavid.vehicleroutingproblem.components;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.VrpKeys;
import org.tomasdavid.vehicleroutingproblem.fragments.VrpFileListFragment;

/**
 * Button for continue to vrp list fragment.
 */
public class OpenFileButton extends Button implements View.OnClickListener {

    public OpenFileButton(Context context) {
        super(context);
        init();
    }

    public OpenFileButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OpenFileButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Sets onClick listener.
     */
    private void init() {
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = (FragmentActivity) v.getContext();
        NumberPicker numberPicker = (NumberPicker) activity.findViewById(R.id.timeLimitPicker);
        Spinner algSpinner = (Spinner) activity.findViewById(R.id.algorithm_spinner);

        // add setting values to bundle and starts new fragment
        Bundle bundle = new Bundle();
        bundle.putInt(VrpKeys.VRP_TIME_LIMIT.name(), numberPicker.getValue());
        bundle.putString(VrpKeys.VRP_ALGORITHM.name(), activity.getResources().obtainTypedArray(R.array.algorithm_files)
                .getString(algSpinner.getSelectedItemPosition()));
        VrpFileListFragment fragment = new VrpFileListFragment();
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, fragment)
                .addToBackStack(null).commit();
    }
}
