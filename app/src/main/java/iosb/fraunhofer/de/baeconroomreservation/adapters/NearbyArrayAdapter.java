package iosb.fraunhofer.de.baeconroomreservation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.entity.NearbyRooms;

/**
 *
 * Created by sakovi on 09.08.2017.
 */

public class NearbyArrayAdapter extends ArrayAdapter
{
    private final Context context;
    public ArrayList<NearbyRooms> values;

    public NearbyArrayAdapter(Context context, ArrayList<NearbyRooms> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_element, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        textView.setText(values.get(position).getBLEid());
        TextView textView1 = (TextView) rowView.findViewById(R.id.secondLine);
        textView1.setText("Distance: " + values.get(position).getDistance()+"m");
        //TODO add ocuupied
        return rowView;
    }

    public void setItems(ArrayList<NearbyRooms> myList) {
        this.values.clear();
        this.values.addAll(myList);
        notifyDataSetChanged();
    }

}
