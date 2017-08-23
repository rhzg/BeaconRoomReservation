package iosb.fraunhofer.de.baeconroomreservation.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.fragments.CalendarFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.RoomListFragment;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

public class MainActivity extends BaseActivity
{
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private static final String AUTH_TOKEN_TYPE = "iosb.fraunhofer.de.baeconroomreservation";
    private static final String AUTH_TOKEN_TYPE_ACC = "full_access";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }

        super.onCreate(savedInstanceState);

        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Void... params) {
                AccountManager am = AccountManager.get(getApplicationContext());
                Account[] accounts = am.getAccountsByType(AUTH_TOKEN_TYPE);
                Account account;
                if (accounts.length > 0) {
                    account = accounts[0];
                    AccountManagerFuture<Bundle> response = am.getAuthToken(account, AUTH_TOKEN_TYPE_ACC, null, getParent(), null, null);
                    try {
                        Bundle b = response.getResult();
                        Communicator.token = b.getString(AccountManager.KEY_AUTHTOKEN);
                        Communicator.setContext(getApplicationContext());
                        return true;
                    } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean start)
            {
                if(start)
                {
                    setNavigationListener();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.root_layout, RoomListFragment.instanceOf(false))
                            .addToBackStack(null)
                            .commit();
                }
            }
        };
        task.execute();


        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
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
                            case R.id.action_favorite:
                                fragment = RoomListFragment.instanceOf(true);
                                break;
                            default:
                                fragment = RoomListFragment.instanceOf(false);
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
