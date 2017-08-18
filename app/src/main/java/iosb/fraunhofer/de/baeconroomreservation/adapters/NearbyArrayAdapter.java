package iosb.fraunhofer.de.baeconroomreservation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

/**
 *
 * Created by sakovi on 09.08.2017.
 */

public class NearbyArrayAdapter extends ArrayAdapter
{
    private final Context context;
    private ArrayList<NerbyResponse> values;

    public NearbyArrayAdapter(Context context, ArrayList<NerbyResponse> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_element, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        textView.setText(values.get(position).getName());

        TextView textView1 = (TextView) rowView.findViewById(R.id.secondLine);
        textView1.setText("Distance:    " + values.get(position).getDistance() + " m");

        ImageView star = (ImageView) rowView.findViewById(R.id.favoriteStar);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Communicator.makeFavorite(values.get(position).getRoomID());
            }
        });

        if(values.get(position).getFavorite())
        {
            star.setImageResource(R.drawable.star_on);
        }else
        {
            star.setImageResource(R.drawable.star_off);
        }

        View view = rowView.findViewById(R.id.occupied);
        if (values.get(position).getOccupied())
        {
            view.setBackgroundColor((ContextCompat.getColor(getContext(), R.color.red_500)));
        }
        //TODO add ocuupied
        return rowView;
    }

    public void  setList(ArrayList<NerbyResponse> values)
    {
        this.values.clear();
        this.values.addAll(values);
        notifyDataSetChanged();
    }
}
