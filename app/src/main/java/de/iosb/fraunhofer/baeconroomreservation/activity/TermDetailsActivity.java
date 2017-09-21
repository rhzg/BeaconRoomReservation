package de.iosb.fraunhofer.baeconroomreservation.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.entity.TermDetails;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;

/**
 *
 * @author Viseslav Sako
 */
public class TermDetailsActivity extends AppCompatActivity
{
    SimpleDateFormat dt;

    @BindView(R.id.startDate) TextView _startDate;
    @BindView(R.id.endDate) TextView _endDate;
    @BindView(R.id.desc) TextView _title;
    @BindView(R.id.location) TextView _location;
    @BindView(R.id.initilaizer) TextView _initilaizer;
    @BindView(R.id.usersInTerm) ListView _users;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        countDownTimer = new CountDownTimer(Constants.timeout, Constants.countdownInterval) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {finish();}
        }.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        TermDetails term = new TermDetails();

        if (b != null)
        {
            term = (TermDetails) b.getSerializable(Constants.term);
        }

        dt = new SimpleDateFormat("HH:mm dd-mm-yyyy");
        setUp(term);
    }

    private void setUp(TermDetails term)
    {
        _endDate.setText(dt.format(term.getEndDate()));
        _initilaizer.setText(term.getInitializer().getName());
        _location.setText(term.getLocation());
        _startDate.setText(dt.format(term.getStartDate()));
        _title.setText(term.getDescription());

        final ArrayAdapter<EntityRepresentation> arrayAdapter = new ArrayAdapter<EntityRepresentation>(this, android.R.layout.simple_list_item_1, term.getUsers());
        _users.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
        return super.onTouchEvent(event);
    }
}
