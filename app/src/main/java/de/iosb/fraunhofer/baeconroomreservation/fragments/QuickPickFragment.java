package de.iosb.fraunhofer.baeconroomreservation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.admin.AuthorizeActivity;

/**
 * This is a class for dialog for dialog that show three possibilities for reserving room.
 * One, two ad three hours. It automatically closes the dialog after predefined period.
 * After choosing duration of reservation it lead to activity where you must authorize yourself.
 *
 * @author Viselav Sako
 */

public class QuickPickFragment extends DialogFragment
{
    public static final long HOUR = 3600*1000;

    @BindView(R.id.one) Button _one;
    @BindView(R.id.two) Button _two;
    @BindView(R.id.three) Button _three;

    //If is 0 than close the dialog
    CountDownTimer countDownTimer;


    /**
     *  This is a method for creating an instance od this fragment.
     *
     * @param roomID of a room to which this app is assigned
     * @return returns a instance of this class
     */
    public static QuickPickFragment newInstance(String roomID)
    {
        QuickPickFragment frag = new QuickPickFragment();
        Bundle args = new Bundle();
        args.putString(Constants.roomID, roomID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        countDownTimer = new CountDownTimer(Constants.timeout, Constants.countdownInterval) {
            public void onTick(long millisUntilFinished) {}
                public void onFinish()
                {
                    if(getDialog() != null)
                    {
                        getDialog().dismiss();
                    }
                }
        }.start();

        return inflater.inflate(R.layout.quick_pick_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, view);

        _one.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                makeReservation(1);
            }
        });

        _two.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                makeReservation(2);
            }
        });

        _three.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                makeReservation(3);
            }
        });
    }


    /**
     * This method is responsible for handling the click of one of the buttons on the dialog.
     * It starts new activity and passes the argument to it.
     *
     * @param duration is defining how long the date will be
     */
    private void makeReservation(int duration)
    {
        countDownTimer.cancel();
        getDialog().dismiss();

        Date date = new Date();
        Date endDate = new Date(date.getTime() + duration * HOUR);

        Intent intent = new Intent(getContext(), AuthorizeActivity.class);
        intent.putExtra(Constants.startDate, date);
        intent.putExtra(Constants.endDate, endDate);
        intent.putExtra(Constants.roomID, getArguments().getString(Constants.roomID));
        intent.putExtra(Constants.title, "Meeting");

        startActivity(intent);
    }

}
