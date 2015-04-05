package org.tomasdavid.vehicleroutingproblem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StatisticsListAdapter extends ArrayAdapter<StatisticItem> {

    private Context context;

    private List<StatisticItem> statisticItems;

    public StatisticsListAdapter(Context context, List<StatisticItem> statisticItems) {
        super(context, R.layout.item_statistic, statisticItems);
        this.context = context;
        this.statisticItems = statisticItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
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
