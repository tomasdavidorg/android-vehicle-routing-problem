package org.tomasdavid.vehicleroutingproblem.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.tomasdavid.vehicleroutingproblem.R;

/**
 * Adapter of vrp file list.
 */
public class VrpFileListAdapter extends RecyclerView.Adapter<VrpFileItemViewHolder> {

    /**
     * Time limit for calculation.
     */
    private int timeLimit;

    /**
     * Algorithm for calculation.
     */
    private String algorithm;

    /**
     * Array of vrp files names.
     */
    private String[] vrpFileNames;

    /**
     * Adapter constructor.
     * @param vrpFileNames Array of vrp files names.
     * @param timeLimit Time limit for calculation.
     * @param algorithm Algorithm for calculation.
     */
    public VrpFileListAdapter(String[] vrpFileNames, int timeLimit, String algorithm) {
        this.vrpFileNames = vrpFileNames;
        this.timeLimit = timeLimit;
        this.algorithm = algorithm;
    }

    /**
     * Returns time limit for calculation.
     * @return Time limit for calculation.
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Returns algorithm for calculation.
     * @return Algorithm for calculation.
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns array of vrp files names.
     * @return Array of vrp files names.
     */
    public String[] getVrpFileNames() {
        return vrpFileNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VrpFileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VrpFileItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_vrp_file, parent, false), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(VrpFileItemViewHolder holder, int position) {
        holder.getFileTextView().setText(vrpFileNames[position]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return vrpFileNames.length;
    }
}
