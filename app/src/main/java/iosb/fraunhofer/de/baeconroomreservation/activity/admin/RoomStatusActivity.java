package iosb.fraunhofer.de.baeconroomreservation.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.activity.BasicActivty;
import iosb.fraunhofer.de.baeconroomreservation.entity.RoomDetailsRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.fragments.QuickPickFragment;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

/**
 *
 * Created by sakovi on 14.09.2017.
 */

public class RoomStatusActivity extends BasicActivty
{
    @BindView(R.id.occupied) TextView _occupied;
    @BindView(R.id.untilTime) TextView _untilTime;
    @BindView(R.id.reserveButton) Button _reserveButton;
    @BindView(R.id.root_layout) LinearLayout _root;

    private String roomname;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomstatus);
        ButterKnife.bind(this);

        roomname = getIntent().getStringExtra("ROOM_NAME");
        Communicator.getRoomDetails(getIntent().getStringExtra("ROOM_ID"), this);
    }

    @Override
    public void setDetails(RoomDetailsRepresentation room)
    {
        _root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RoomStatusCalendarActivity.class);
                intent.putExtra("ROOM_NAME", roomname);
                intent.putExtra("ROOM_ID", getIntent().getStringExtra("ROOM_ID"));
                startActivity(intent);
            }
        });

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
        QuickPickFragment quickPickFragment = QuickPickFragment.newInstance(getIntent().getStringExtra("ROOM_ID"));
        quickPickFragment.show(fm, "fragment_edit_name");
    }


    @Override
    public void onBackPressed() {
    }
}
