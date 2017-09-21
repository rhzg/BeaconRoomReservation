package de.iosb.fraunhofer.baeconroomreservation.activity.admin;

import android.os.Bundle;

import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.BaseActivity;

/**
 * This is activity where you can pick a room which will be the room to which this device is assigned.
 *
 * @author Viseslav Sako
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
