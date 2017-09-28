package de.iosb.fraunhofer.baeconroomreservation.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.admin.AuthorizeActivity;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.fragments.PickUserFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.DatePickerFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.TimePickerFragment;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;


/**
 * This is an activity that is used for reserving room.
 *
 * @author Viseslav Sako
 */
public class RoomReservationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener ,DatePickerDialog.OnDateSetListener
{

    @BindView(R.id.startTime) TextView _startTime;
    @BindView(R.id.endTime) TextView _endTime;
    @BindView(R.id.title) EditText _title;
    @BindView(R.id.users) TextView _users;
    @BindView(R.id.reserveButtin) Button _reserve;
    @BindView(R.id.date) TextView _date;
    private boolean start;
    private ArrayList<EntityRepresentation> entityRepresentations = new ArrayList<>();
    private ArrayList<String> sendIds;
    CountDownTimer countDownTimer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_room);
        final String room_id = getIntent().getStringExtra(Constants.roomID);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.roomName));
        ButterKnife.bind(this);

        sendIds = new ArrayList<>();

        initFields();
        _date.setShowSoftInputOnFocus(false);
        _startTime.setShowSoftInputOnFocus(false);
        _endTime.setShowSoftInputOnFocus(false);

        _startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), Constants.startTime);
                start = true;
                restartTimer();
            }
        });

        _users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PickUserFragment boxDialogFragment = PickUserFragment.newInstance(entityRepresentations);
                boxDialogFragment.setActivity(RoomReservationActivity.this);
                boxDialogFragment.show(getSupportFragmentManager(), "users");
                restartTimer();
            }
        });

        _endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), Constants.endTime);
                start = false;
                restartTimer();
            }
        });

        _date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), Constants.date);
                restartTimer();
            }
        });

        _reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput())
                {
                    if(getIntent().getBooleanExtra(Constants.authorize, false))
                    {
                        makeReservation();
                    }
                    else {
                        Communicator.roomReservation(_startTime.getText().toString(),
                                _endTime.getText().toString(), _date.getText().toString(),
                                room_id, _title.getText().toString(), sendIds, RoomReservationActivity.this);
                    }
                }else
                {
                    Toast.makeText(RoomReservationActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                restartTimer();
            }
        });
    }

    private void makeReservation()
    {
        Intent intent = new Intent(this, AuthorizeActivity.class);
        intent.putExtra(Constants.startTime, _startTime.getText().toString());
        intent.putExtra(Constants.endTime,_endTime.getText().toString());
        intent.putExtra(Constants.roomID, getIntent().getStringExtra(Constants.roomID));
        intent.putExtra(Constants.roomName, getIntent().getStringExtra(Constants.roomName));
        intent.putExtra(Constants.title, _title.getText().toString());
        intent.putExtra(Constants.date,  _date.getText().toString());
        intent.putExtra(Constants.ids,  sendIds);
        startActivity(intent);
    }

    private void restartTimer()
    {
        if(getIntent().getBooleanExtra(Constants.authorize, false)) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
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

        if(_title.getText().toString().isEmpty())
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
        int minute = c.get(Calendar.MINUTE );
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        _endTime.setText(String.format("%" +
                "02d",(hour + 1)%24)+":"+String.format("%02d", minute));
        _startTime.setText(String.format("%02d", hour)+":"+String.format("%02d", minute));
        _date.setText(dayOfMonth+"."+(month+1)+"."+year);

        if(getIntent().getBooleanExtra(Constants.authorize, false))
        {
            countDownTimer = new CountDownTimer(Constants.timeout, Constants.countdownInterval) {
                public void onTick(long millisUntilFinished) {}
                public void onFinish() {finish();}
            }.start();
        }
    }

    public void setUserText()
    {
        _users.setText("");
        sendIds.clear();
        for (int i = 0; i< entityRepresentations.size(); i++)
        {
            if(i == entityRepresentations.size()-1)
            {
                _users.append(entityRepresentations.get(i).getName());

            }else
            {
                _users.append(entityRepresentations.get(i).getName()+", ");
            }
            sendIds.add(entityRepresentations.get(i).getUserID());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(getIntent().getBooleanExtra(Constants.authorize, false)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                countDownTimer.cancel();
                countDownTimer.start();
            }
        }
        return super.onTouchEvent(event);
    }
}
