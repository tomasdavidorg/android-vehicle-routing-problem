package org.tomasdavid.vehicleroutingproblem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import org.tomasdavid.vehicleroutingproblem.components.AboutAppDialog;
import org.tomasdavid.vehicleroutingproblem.components.LegendDialog;
import org.tomasdavid.vehicleroutingproblem.R;

/**
 * Main fragment with settings.
 *
 * @author Tomas David
 */
public class MainFragment extends Fragment {

    /**
     * Default time limit in number picker.
     */
    private static final int DEFAULT_TIME_LIMIT = 10;

    /**
     * Minimum time limit in number picker.
     */
    private static final int MIN_TIME_LIMIT = 1;

    /**
     * Maximum time limit in number picker.
     */
    private static final int MAX_TIME_LIMIT = 1000;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // time limit number picker initialization
        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.timeLimitPicker);
        numberPicker.setMinValue(MIN_TIME_LIMIT);
        numberPicker.setMaxValue(MAX_TIME_LIMIT);
        numberPicker.setValue(DEFAULT_TIME_LIMIT);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // welcome text
        TextView welcomeText = (TextView) view.findViewById(R.id.welcome_text);
        welcomeText.setText(Html.fromHtml(getString(R.string.welcome_text)));

        // spinner for choosing algorithm initialization
        Spinner algSpinner = (Spinner) view.findViewById(R.id.algorithm_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.algorithms,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algSpinner.setAdapter(spinnerAdapter);

        setHasOptionsMenu(true);
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vrp, menu);

        // disable play button
        menu.findItem(R.id.action_run).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AboutAppDialog aad = new AboutAppDialog();
            aad.show(getActivity().getSupportFragmentManager(), null);
            return true;
        } else if (id == R.id.action_legend) {
            LegendDialog ld = new LegendDialog();
            ld.show(getActivity().getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
