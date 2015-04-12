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
