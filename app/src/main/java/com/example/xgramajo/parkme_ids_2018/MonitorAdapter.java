package com.example.xgramajo.parkme_ids_2018;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MonitorAdapter extends ArrayAdapter<ParkingItem> {

    public MonitorAdapter(Context context, ArrayList<ParkingItem> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ParkingItem parkingItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.monitor_item, parent, false);
        }

        // Lookup view for data population
        TextView patent = (TextView) convertView.findViewById(R.id.item_patent);
        TextView direction = (TextView) convertView.findViewById(R.id.item_direction);
        TextView time = convertView.findViewById(R.id.item_time);

        // Populate the data into the template view using the data object
        assert parkingItem != null;
        patent.setText(parkingItem.patent);
        direction.setText(parkingItem.direction);
        time.setText(parkingItem.timeLeft);

        // Return the completed view to render on screen
        return convertView;
    }
}
