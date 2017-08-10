package iosb.fraunhofer.de.baeconroomreservation.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.fragments.CalendarFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.RoomListFragment;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, RoomListFragment.instanceOf(), "rageComicList")
                    .commit();
        }

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        setNavigationListener();
    }

    private void setNavigationListener()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                fragment = CalendarFragment.instanceOf();
                                break;
                            case R.id.action_search:
                                fragment = CalendarFragment.instanceOf();
                                break;
                            default:
                                fragment = RoomListFragment.instanceOf();
                        }
                        switchToFragment(fragment);
                        return true;
                    }
                });
    }

    private void switchToFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean havePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }
}
