package iosb.fraunhofer.de.baeconroomreservation.activity.admin;

import android.os.Bundle;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.activity.BaseActivity;

/**
 *
 * Created by sakovi on 13.09.2017.
 */

public class PickRoomActivity extends BaseActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickroom);
    }


    @Override
    protected int getNavigationDrawerID() {
        checkIfAdmin();
        return R.id.nav_admin;
    }

}
