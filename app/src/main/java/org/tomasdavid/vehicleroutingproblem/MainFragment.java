package org.tomasdavid.vehicleroutingproblem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = (Button) view.findViewById(R.id.button_open_file);
        button.setOnClickListener(new OpenFileButtonClick());
        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.timeLimitPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);
        numberPicker.setValue(1);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        TextView welcomeText = (TextView) view.findViewById(R.id.welcome_text);
        welcomeText.setText(Html.fromHtml(getString(R.string.welcome_text)));

        Spinner algSpinner = (Spinner) view.findViewById(R.id.algorithm_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.algorithms, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algSpinner.setAdapter(spinnerAdapter);
        return view;
    }

    private class OpenFileButtonClick implements OnClickListener {

        @Override
        public void onClick(View v) {

            NumberPicker numberPicker = (NumberPicker) getActivity().findViewById(R.id.timeLimitPicker);
            Spinner algSpinner = (Spinner) getActivity().findViewById(R.id.algorithm_spinner);

            Bundle b = new Bundle();
            b.putInt(VrpKeys.VRP_TIME_LIMIT.name(), numberPicker.getValue());
            b.putString(VrpKeys.VRP_ALGORITHM.name(), getActivity().getResources().obtainTypedArray(R.array.algorithm_files).getString(algSpinner.getSelectedItemPosition()));

            VrpFileListFragment a = new VrpFileListFragment();
            a.setArguments(b);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main, a);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
