package de.iosb.fraunhofer.baeconroomreservation.fragments;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.adapters.Event;
import de.iosb.fraunhofer.baeconroomreservation.entity.Term;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This fragment is responsible for showing calendar with dates.
 * Dates can be of one user or for multiple rooms (favorite).
 *
 * @author Viselsav Sako
 */
public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekViewLoader
{
    private WeekView mWeekView;
    private Button favoriteRooms;
    private Button myTerms;
    private List<Term> terms = new ArrayList<>();
    private ProgressDialog progressDialog;

    public static CalendarFragment instanceOf()
    {
        return new CalendarFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Communicator.getFavoriteDates(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        mWeekView = (WeekView) root.findViewById(R.id.weekView);
        favoriteRooms = (Button) root.findViewById(R.id.favoriteTerms);
        myTerms = (Button) root.findViewById(R.id.myTerms);

        favoriteRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressDialog.show();
                favoriteRooms.setBackgroundResource(R.color.grey_600);
                myTerms.setBackgroundResource(R.color.grey_400);
                Communicator.getFavoriteDates(CalendarFragment.this);
            }
        });

        myTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressDialog.show();
                favoriteRooms.setBackgroundResource(R.color.grey_400);
                myTerms.setBackgroundResource(R.color.grey_600);
                Communicator.getMyDates(CalendarFragment.this);
            }
        });

        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.setWeekViewLoader(this);
        mWeekView.goToHour(new Date().getHours());

        progressDialog = new ProgressDialog(super.getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading terms...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        return root;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth)
    {
        List<WeekViewEvent> weekViewEvents = new ArrayList<>();
        return weekViewEvents;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {}

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect)
    {
        Communicator.setContext(getContext());
        Communicator.getTermDetails(((Event) event).getTerm());
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {}


    /**
     * This methods is for setting up the calendar view with dates and refreshing the UI.
     *
     * @param terms list of dates that will bw shown in calendar view.
     */
    public void setTerms(List<Term> terms)
    {
        this.terms = terms;
        mWeekView.notifyDatasetChanged();
        progressDialog.dismiss();
    }

    @Override
    public double toWeekViewPeriodIndex(Calendar instance)
    {
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
        return weekViewEvents;
    }

    public void fail()
    {
        progressDialog.hide();
        Toast.makeText(getActivity(), "Ups something went wrong", Toast.LENGTH_SHORT).show();
    }
}
