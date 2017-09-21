package de.iosb.fraunhofer.baeconroomreservation.activity.admin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.BasicActivity;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomDetailsRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.fragments.QuickPickFragment;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This is an activity in which is used for displaying status of the room.
 * This is the root activity for when the tablet is used for showing the status of some room.
 *
 * Created by sakovi on 14.09.2017.
 */
public class RoomStatusActivity extends BasicActivity
{
    private static final String AUTH_TOKEN_TYPE = "de.fraunhofer.iosb.baeconroomreservation";
    private static final long refreshInterval = 5;

    @BindView(R.id.occupied) TextView _occupied;
    @BindView(R.id.untilTime) TextView _untilTime;
    @BindView(R.id.reserveButton) Button _reserveButton;
    @BindView(R.id.root_layout) LinearLayout _root;

    private String roomname;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        AccountManager am = AccountManager.get(this);
        Account accounts[] = am.getAccountsByType(AUTH_TOKEN_TYPE);
        if(accounts.length != 0)
        {
            Account account = accounts[0];
            am.removeAccountExplicitly(account);
        }

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_roomstatus);
        ButterKnife.bind(this);

        roomname = getIntent().getStringExtra(Constants.roomName);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Communicator.getRoomDetails(getIntent().getStringExtra(Constants.roomID), RoomStatusActivity.this);
            }
        }, 0L, refreshInterval, TimeUnit.SECONDS);
    }

    @Override
    public void setDetails(RoomDetailsRepresentation room)
    {
        _root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RoomStatusCalendarActivity.class);
                intent.putExtra(Constants.roomName, roomname);
                intent.putExtra(Constants.roomID, getIntent().getStringExtra(Constants.roomID));
                startActivity(intent);
            }
        });

        _root.setVisibility(View.VISIBLE);

        _reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showEditDialog();
            }
        });

        if (room.isOccupied())
        {
            _reserveButton.setEnabled(false);
            _untilTime.setText("Occupied until: " + room.getUntil());
            _occupied.setText(roomname + " is occupied");
            _occupied.setBackgroundColor(getResources().getColor(R.color.red_500));
            _untilTime.setBackgroundColor(getResources().getColor(R.color.red_500));
        }else
        {
            _reserveButton.setEnabled(true);
            _untilTime.setText("Available until: " + room.getUntil());
            _occupied.setText(roomname + " is free");
            _occupied.setBackgroundColor(getResources().getColor(R.color.green_500));
            _untilTime.setBackgroundColor(getResources().getColor(R.color.green_500));
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        QuickPickFragment quickPickFragment = QuickPickFragment.newInstance(getIntent().getStringExtra(Constants.roomID));
        quickPickFragment.show(fm, "fragment_edit_name");
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Communicator.getRoomDetails(getIntent().getStringExtra(Constants.roomID), this);
    }
}
