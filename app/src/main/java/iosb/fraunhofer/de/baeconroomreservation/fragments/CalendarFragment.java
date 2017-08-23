package iosb.fraunhofer.de.baeconroomreservation.fragments;

import android.app.ProgressDialog;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.entity.Term;

import static iosb.fraunhofer.de.baeconroomreservation.rest.Communicator.getfavoritesTerms;

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekViewLoader {
    private WeekView mWeekView;
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
        getfavoritesTerms(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.calendar_fragment, container, false);
        mWeekView = (WeekView) root.findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.setWeekViewLoader(this);

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
        int i = 0;

        Calendar startTime = Calendar.getInstance();
        Calendar endTime = (Calendar) startTime.clone();

        for (Term term : terms)
        {
            startTime.setTimeInMillis(Long.parseLong(term.getStartDate()));

            endTime.setTimeInMillis(Long.parseLong(term.getEndDate()));

            WeekViewEvent weekViewEvent = new WeekViewEvent(i, term.getDescription(), term.getLocation(), startTime, endTime);
            weekViewEvent.setColor(getResources().getColor(R.color.red_500));
            weekViewEvents.add(weekViewEvent);
            i++;
        }
        return weekViewEvents;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {}

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {}

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {}


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
        List<WeekViewEvent> weekViewEvents = new ArrayList<>();
        if(periodIndex == 0){
            int i = 0;


            for (Term term : terms)
            {
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = (Calendar) startTime.clone();
                startTime.setTimeInMillis(Long.parseLong(term.getStartDate()));
                endTime.setTimeInMillis(Long.parseLong(term.getEndDate()));

                WeekViewEvent weekViewEvent = new WeekViewEvent(i, term.getDescription(), term.getLocation(), startTime, endTime);
                weekViewEvent.setColor(getResources().getColor(R.color.red_500));
                weekViewEvents.add(weekViewEvent);
                i++;
            }
        }
        return weekViewEvents;
    }
}
