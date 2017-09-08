package iosb.fraunhofer.de.baeconroomreservation.activity;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.adapters.Event;
import iosb.fraunhofer.de.baeconroomreservation.entity.RoomDetailsRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.entity.Term;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

/**
 *
 * Created by sakovi on 08.09.2017.
 */

public class RoomDetailsActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekViewLoader
{
    @BindView(R.id.weekView) WeekView weekView;
    @BindView(R.id.occupied) TextView _occupied;
    @BindView(R.id.action_favorite) Button _btnFavorite;


    private List<Term> terms = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getIntent().getStringExtra("ROOM_NAME"));
        Communicator.getRoomDetails(getIntent().getStringExtra("ROOM_ID"), this);

        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setEventLongPressListener(this);
        weekView.setEmptyViewLongPressListener(this);
        weekView.setWeekViewLoader(this);
        weekView.goToHour(new Date().getHours());
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth)
    {
        List<WeekViewEvent> weekViewEvents = new ArrayList<>();
        return weekViewEvents;    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect)
    {
        Communicator.setContext(this);
        Communicator.getTerm(((Event) event).getTerm());
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public double toWeekViewPeriodIndex(Calendar instance) {
        return 0;
    }

    @Override
    public List<? extends WeekViewEvent> onLoad(int periodIndex)
    {
        List<Event> weekViewEvents = new ArrayList<>();
        Resources res = getResources();
        TypedArray colorsRes = res.obtainTypedArray(R.array.calendarColors);
        Map<String, Integer> colors = new HashMap<>();
        if(periodIndex == 0){
            int i = 0;

            for (Term term : terms)
            {
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = (Calendar) startTime.clone();
                startTime.setTime(term.getStartDate());
                endTime.setTime(term.getEndDate());
                Event weekViewEvent = new Event(i, term.getDescription(), term.getLocation(), startTime, endTime, term);
                if(colors.containsKey(term.getLocation()))
                {
                    weekViewEvent.setColor(colors.get(term.getLocation()));
                }else
                {
                    weekViewEvent.setColor(colorsRes.getColor(i, 0));
                    colors.put(term.getLocation(), colorsRes.getColor(i, 0));
                    i++;
                }
                weekViewEvents.add(weekViewEvent);
            }
        }
        return weekViewEvents;    }


    public void setDetails(RoomDetailsRepresentation room)
    {
        if(room.isFavorite())
        {
            _btnFavorite.setBackgroundResource(R.drawable.star_on);
        }else
        {
            _btnFavorite.setBackgroundResource(R.drawable.star_off);
        }

        if (room.isOccupied())
        {
            _occupied.setText("OCCUPIED");
//            _occupied.setTextColor(getColor(R.color.red_500));
        }else
        {
            _occupied.setText("FREE");
//            _occupied.setTextColor(getColor(R.color.green_500));
        }
        this.terms = room.getTerms();
        weekView.notifyDatasetChanged();
    }
}
