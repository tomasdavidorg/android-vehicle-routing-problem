package org.tomasdavid.vehicleroutingproblem;

import android.os.AsyncTask;
import android.widget.ProgressBar;

public class ProgressBarTask extends AsyncTask<Integer, Void, Void> {

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
                e.printStackTrace();
            }
            progressBar = (ProgressBar) fragment.getActivity().findViewById(R.id.progress_bar);
            progressBar.incrementProgressBy(1);
            progress++;
        }

        return null;
    }
}
