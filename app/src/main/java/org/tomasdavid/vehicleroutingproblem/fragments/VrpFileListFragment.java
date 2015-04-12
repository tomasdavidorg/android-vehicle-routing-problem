package org.tomasdavid.vehicleroutingproblem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.VrpFileListAdapter;
import org.tomasdavid.vehicleroutingproblem.VrpKeys;

import java.io.IOException;
import java.util.ArrayList;

public class VrpFileListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vrp_file_list, container, false);

        LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.vrp_file_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> vrpAssets = new ArrayList<>();
        String[] assetsRoot;

        // get all assets
        try {
            assetsRoot = getActivity().getAssets().list(getActivity().getString(R.string.vrps_dir));
        } catch (IOException e) {
            assetsRoot = new String[0];
        }

        // remove files without vrp file extension
        for (String asset : assetsRoot) {
            if (asset.endsWith(".vrp")) {
                vrpAssets.add(asset);
            }
        }

        VrpFileListAdapter mAdapter = new VrpFileListAdapter(vrpAssets.toArray(new String[vrpAssets.size()]), getActivity().getSupportFragmentManager());
        mAdapter.setTimeLimit(getArguments().getInt(VrpKeys.VRP_TIME_LIMIT.name()));
        mAdapter.setAlgorithm(getArguments().getString(VrpKeys.VRP_ALGORITHM.name()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
