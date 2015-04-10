package org.tomasdavid.vehicleroutingproblem;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ProgressBar;

public class ProgressBarTask extends AsyncTask<Integer, Void, Void> {

    private static final String TAG = "ProgressBarTask";

    private int progress;

    private VrpFragment fragment;

    public ProgressBarTask(VrpFragment fragment) {
        this.fragment = fragment;
        this.progress = 0;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        progress = 0;
        ProgressBar progressBar = (ProgressBar) fragment.getActivity().findViewById(R.id.progress_bar);
        progressBar.setProgress(progress);

        while (fragment.getVrpSolverTask().isRunning() && progress < progressBar.getMax()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread sleep error.", e);
            }

            if (fragment != null) {
                FragmentActivity activity =  fragment.getActivity();
                if (activity != null) {
                    progressBar = (ProgressBar) activity.findViewById(R.id.progress_bar);
                    if (progressBar != null) {
                        progressBar.incrementProgressBy(1);
                    }
                }
            }
            progress++;
        }

        return null;
    }
}
