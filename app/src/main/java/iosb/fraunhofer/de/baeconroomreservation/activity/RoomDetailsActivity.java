package iosb.fraunhofer.de.baeconroomreservation.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.fragments.CheckBoxDialogFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.DatePickerFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.TimePickerFragment;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;


/**
 * Created by sakovi on 10.08.2017.
 *
 */


public class RoomDetailsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener
{

    @BindView(R.id.startTime) EditText _startTime;
    @BindView(R.id.endTime) EditText _endTime;
    @BindView(R.id.title) EditText _title;
    @BindView(R.id.users) EditText _users;
    @BindView(R.id.reserveButtin) Button _reserv;
    @BindView(R.id.date) EditText _date;
    private boolean start;
    private ArrayList<UserRepresentation> userRepresentations;
    private ArrayList<String> sendIds;
    private boolean[] checkedItems;


    public void setUserRepresentations(ArrayList<UserRepresentation> userRepresentations) {
        this.userRepresentations = userRepresentations;
        checkedItems = new boolean[userRepresentations.size()];
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_room);
        final String room_id = getIntent().getStringExtra("ROOM_ID");
        getSupportActionBar().setTitle(getIntent().getStringExtra("ROOM_NAME"));
        ButterKnife.bind(this);

        Communicator.userGet(this);

        sendIds = new ArrayList<>();

        initFields();
        _date.setShowSoftInputOnFocus(false);
        _users.setShowSoftInputOnFocus(false);
        _startTime.setShowSoftInputOnFocus(false);
        _endTime.setShowSoftInputOnFocus(false);

        _startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "startTime");
                start = true;
            }
        });

        _users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CheckBoxDialogFragment boxDialogFragment = CheckBoxDialogFragment.newInstance(checkedItems, userRepresentations);
                boxDialogFragment.setActivity(RoomDetailsActivity.this);
                boxDialogFragment.show(getSupportFragmentManager(), "users");
            }
        });

        _endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "endTime");
                start = false;
            }
        });

        _date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "date");
            }
        });

        _reserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput())
                {
                    Communicator.roomReservation(_startTime.getText().toString(),
                            _endTime.getText().toString(), _date.getText().toString(),
                            room_id, _title.getText().toString(), sendIds, RoomDetailsActivity.this);
                }else
                {
                    Toast.makeText(RoomDetailsActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public boolean validateInput()
    {
        String[] startTimeSeparate = _startTime.getText().toString().split(":");
        int hourS = Integer.parseInt(startTimeSeparate[0]);
        int minuteS = Integer.parseInt(startTimeSeparate[1]);

        String[] endTimeSeparate = _endTime.getText().toString().split(":");
        int hourE = Integer.parseInt(endTimeSeparate[0]);
        int minuteE = Integer.parseInt(endTimeSeparate[1]);

        if(hourS*60+minuteS >= hourE*60+minuteE)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        if(start)
        {
            _startTime.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
        }
        else
        {
            _endTime.setText(String.format("%" +
                    "02d", hourOfDay)+":"+String.format("%02d", minute));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        _date.setText(dayOfMonth+"."+(month+1)+"."+year);
    }

    public void initFields()
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        _endTime.setText(String.format("%" +
                "02d", hour)+":"+String.format("%02d", minute));
        _startTime.setText(String.format("%02d", hour)+":"+String.format("%02d", minute));
        _date.setText(dayOfMonth+"."+(month+1)+"."+year);
    }

    public void setUserText()
    {
        _users.setText("");
        sendIds.clear();
        for (int i = 0; i<userRepresentations.size(); i++)
        {
            if(checkedItems[i])
            {
                _users.getText().append(userRepresentations.get(i).getName()+", ");
                sendIds.add(userRepresentations.get(i).getUserID());
            }
        }
    }

}
