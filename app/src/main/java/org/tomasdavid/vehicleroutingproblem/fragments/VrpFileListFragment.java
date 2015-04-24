/*
 * Copyright 2015 Tomas David
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tomasdavid.vehicleroutingproblem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tomasdavid.vehicleroutingproblem.R;
import org.tomasdavid.vehicleroutingproblem.VrpKeys;
import org.tomasdavid.vehicleroutingproblem.adapters.VrpFileListAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Fragment for displaying list of vrp files.
 *
 * @author Tomas David
 */
public class VrpFileListFragment extends Fragment {

    /**
     * Extension of vrp file.
     */
    private static final String VRP_EXTENSION = ".vrp";

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vrp_file_list, container, false);

        // initialize recycler view
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.vrp_file_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            if (asset.endsWith(VRP_EXTENSION)) {
                vrpAssets.add(asset);
            }
        }

        // initialize adapter
        VrpFileListAdapter mAdapter = new VrpFileListAdapter(
                vrpAssets.toArray(new String[vrpAssets.size()]),
                getArguments().getInt(VrpKeys.VRP_TIME_LIMIT.name()),
                getArguments().getString(VrpKeys.VRP_ALGORITHM.name())
        );
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
