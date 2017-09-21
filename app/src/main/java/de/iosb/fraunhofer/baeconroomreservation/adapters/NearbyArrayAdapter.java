package de.iosb.fraunhofer.baeconroomreservation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomOverview;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This is an adpter for ListView that is used to display room information.
 *
 * @author Viseslav Sako
 */

public class NearbyArrayAdapter extends ArrayAdapter
{
    private final Context context;
    private ArrayList<RoomOverview> values;
    private static final long HALF_HOUR = 1800*1000;
    private SimpleDateFormat dt;

    public NearbyArrayAdapter(Context context, ArrayList<RoomOverview> values)
    {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        dt = new SimpleDateFormat("HH:mm dd-mm-yyyy");

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
        if(values.get(position).getDistance() != 0.0d)
        {
            String distance = BigDecimal.valueOf(values.get(position).getDistance()).setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
            textView1.setText("Distance: " + distance + " m");
        }
        else
        {
            textView1.setText(R.string.notInRange);
        }

        ImageView star = (ImageView) rowView.findViewById(R.id.favoriteStar);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Communicator.makeFavorite(values.get(position).getRoomID(), context);
            }
        });

        if(values.get(position).getFavorite())
        {
            star.setImageResource(R.drawable.star_on);
        }else
        {
            star.setImageResource(R.drawable.star_off);
        }

        TextView dateStart = (TextView) rowView.findViewById(R.id.dateStart);

        Date dateEnd = new Date(Long.parseLong(values.get(position).getUntil()));

        TextView dateEndTxt = (TextView) rowView.findViewById(R.id.dateEnd);
        dateEndTxt.setText("Until: " +dt.format( new Date(Long.parseLong(values.get(position).getUntil()))));

        View view = rowView.findViewById(R.id.occupied);
        if (values.get(position).getOccupied())
        {
            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date half = new Date(t + HALF_HOUR);
            if(dateEnd.after(half))
            {
                view.setBackgroundColor((ContextCompat.getColor(getContext(), R.color.red_500)));
            }
            else {
                view.setBackgroundColor((ContextCompat.getColor(getContext(), R.color.yellow_700)));
            }
            dateStart.setText("From: " + dt.format(new Date(Long.parseLong(values.get(position).getFrom()))));
            dateEndTxt.setText("Until: " + dt.format(new Date(Long.parseLong(values.get(position).getUntil()))));
        }
        else
        {
            dateStart.setText("Available");
            dateEndTxt.setText("Until: " + dt.format(new Date(Long.parseLong(values.get(position).getFrom()))));
        }
        return rowView;
    }

    /**
     * This method is used for refreshing the list of elements of the list view.
     *
     * @param values new elements that must been shown in the list view
     */
    public void setList(ArrayList<RoomOverview> values)
    {
        this.values.clear();
        this.values.addAll(values);
        notifyDataSetChanged();
    }
}
