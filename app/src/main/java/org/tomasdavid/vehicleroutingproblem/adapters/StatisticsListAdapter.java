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

package org.tomasdavid.vehicleroutingproblem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tomasdavid.vehicleroutingproblem.R;

import java.util.List;

/**
 * Adapter for statistic items of navigation drawer.
 *
 * @author Tomas David
 */
public class StatisticsListAdapter extends ArrayAdapter<StatisticItem> {

    /**
     * Adapter context.
     */
    private Context context;

    /**
     * List of statistic items.
     */
    private List<StatisticItem> statisticItems;

    /**
     * Constructor for statistics list adapter.
     * @param context Adapter context.
     * @param statisticItems List of statistic items.
     */
    public StatisticsListAdapter(Context context, List<StatisticItem> statisticItems) {
        super(context, R.layout.item_statistic, statisticItems);
        this.context = context;
        this.statisticItems = statisticItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        // sets parameters of row
        View rowView = inflater.inflate(R.layout.item_statistic, null, true);
        ImageView image = (ImageView) rowView.findViewById(R.id.item_statistic_image);
        TextView name = (TextView) rowView.findViewById(R.id.item_statistic_name);
        TextView value = (TextView) rowView.findViewById(R.id.item_statistic_value);
        image.setImageResource(statisticItems.get(position).getImageResId());
        name.setText(statisticItems.get(position).getName());
        value.setText(statisticItems.get(position).getValue());

        return rowView;
    }
}
