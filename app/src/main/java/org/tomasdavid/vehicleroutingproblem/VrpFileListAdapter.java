package org.tomasdavid.vehicleroutingproblem;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class VrpFileListAdapter extends RecyclerView.Adapter<VrpFileListAdapter.VrpFileItemViewHolder> {

    private String[] vrpFileNames;

    private FragmentManager fragmentManager;

    public VrpFileListAdapter(String[] vrpFileNames, FragmentManager fragmentManager) {
        this.vrpFileNames = vrpFileNames;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public VrpFileItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VrpFileItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vrp_file, parent, false));
    }

    @Override
    public void onBindViewHolder(VrpFileItemViewHolder holder, int position) {
        holder.getFileTextView().setText(vrpFileNames[position]);
    }

    @Override
    public int getItemCount() {
        return vrpFileNames.length;
    }

    public class VrpFileItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView fileTextView;

        public VrpFileItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new OnItemClick());
            fileTextView = (TextView) itemView.findViewById(R.id.vrp_file_item);
        }

        public TextView getFileTextView() {
            return fileTextView;
        }

        private class OnItemClick implements OnClickListener {

            @Override
            public void onClick(View v) {
                VrpFragment fragment = new VrpFragment();
                Bundle b = new Bundle();
                b.putString("FILE", vrpFileNames[getLayoutPosition()]);
                fragment.setArguments(b);
                fragmentManager.beginTransaction().replace(R.id.activity_main, fragment).addToBackStack(null).commit();
            }
        }
    }
}
