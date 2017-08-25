package iosb.fraunhofer.de.baeconroomreservation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.entity.TermDetails;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;

/**
 *
 * Created by sakovi on 24.08.2017.
 */

public class TermDetailsActivity extends AppCompatActivity
{

    @BindView(R.id.startDate) TextView _startDate;
    @BindView(R.id.endDate) TextView _endDate;
    @BindView(R.id.desc) TextView _title;
    @BindView(R.id.location) TextView _location;
    @BindView(R.id.initilaizer) TextView _initilaizer;
    @BindView(R.id.usersInTerm) ListView _users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        TermDetails term = new TermDetails();
        if (b != null){
            term = (TermDetails) b.getSerializable("term");
        }
        setUp(term);
    }

    private void setUp(TermDetails term)
    {
        _endDate.setText(term.getEndDate().toString());
        _initilaizer.setText(term.getInitilaizer().getName());
        _location.setText(term.getLocation());
        _startDate.setText(term.getStartDate().toString());
        _title.setText(term.getDescription());

        final ArrayAdapter<UserRepresentation> arrayAdapter = new ArrayAdapter<UserRepresentation>(this, android.R.layout.simple_list_item_1, term.getUsers());
        _users.setAdapter(arrayAdapter);
    }


}
