package org.tomasdavid.vehicleroutingproblem.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.VrpKeys;
import org.tomasdavid.vehicleroutingproblem.fragments.VrpFragment;

/**
 * Vrp file item view holder for vrp list of files.
 */
public class VrpFileItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * File text view.
     */
    private TextView fileTextView;

    /**
     * Vrp file list adapter.
     */
    private VrpFileListAdapter adapter;

    /**
     * Constructor of file item holder.
     * @param itemView File text view.
     * @param adapter Vrp file list adapter.
     */
    public VrpFileItemViewHolder(View itemView, VrpFileListAdapter adapter) {
        super(itemView);
        this.fileTextView = (TextView) itemView.findViewById(R.id.vrp_file_item);
        this.adapter = adapter;
        itemView.setOnClickListener(this);
    }

    /**
     * Returns file text view.
     * @return File text view
     */
    public TextView getFileTextView() {
        return fileTextView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {

        // add values to bundle
        Bundle bundle = new Bundle();
        bundle.putInt(VrpKeys.VRP_TIME_LIMIT.name(), adapter.getTimeLimit());
        bundle.putString(VrpKeys.VRP_ALGORITHM.name(), adapter.getAlgorithm());
        bundle.putString(VrpKeys.VRP_FILE_NAME.name(), adapter.getVrpFileNames()[getLayoutPosition()]);

        // start vrp fragment
        VrpFragment fragment = new VrpFragment();
        fragment.setArguments(bundle);
        ((FragmentActivity) v.getContext()).getSupportFragmentManager()
                .beginTransaction().replace(R.id.activity_main, fragment).addToBackStack(null).commit();
    }
}
