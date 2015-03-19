package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;


public class FileListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.file_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> vrpAssets = new ArrayList<>();
        try {
            String[] assetsRoot = this.getAssets().list("");
            for (String asset : assetsRoot) {
                if (asset.endsWith(".vrp")) {
                    vrpAssets.add(asset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAdapter = new MyAdapter(vrpAssets.toArray(new String[vrpAssets.size()]));

        mRecyclerView.setAdapter(mAdapter);
    }
}