package iosb.fraunhofer.de.baeconroomreservation.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
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
import android.view.View;

import java.io.IOException;

import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.fragments.CalendarFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.RoomListFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.SearchFragment;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

public class MainActivity extends BaseActivity
{
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private static final String ACCOUNT_TYPE = "iosb.fraunhofer.de.baeconroomreservation";
    private static final String AUTH_TOKEN_TYPE_ACC_REG = "read_only";
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
                Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
                Account account;
                if (accounts.length > 0) {
                    account = accounts[0];
                    AccountManagerFuture<Bundle> response;
                    am.invalidateAuthToken(ACCOUNT_TYPE,Communicator.token);
                    response = am.getAuthToken(account, AUTH_TOKEN_TYPE_ACC_REG, null, MainActivity.this, null, null);
                    try {
                        Bundle b = response.getResult();
                        Communicator.admin = Boolean.parseBoolean(am.getUserData(account, "ADMIN"));
                        Communicator.token = b.getString(AccountManager.KEY_AUTHTOKEN);
                        Communicator.setContext(getApplicationContext());
                        return true;
                    } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    am.addAccount(ACCOUNT_TYPE, AUTH_TOKEN_TYPE_ACC_REG, null, null, MainActivity.this, null, null);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean start)
            {
                if(start)
                {
                    setNavigationListener();
                    checkIfAdmin();
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

    private void checkIfAdmin()
    {
        MenuItem item = mNavigationView.getMenu().findItem(R.id.nav_admin);
        item.setVisible(Communicator.admin);
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
                                fragment = SearchFragment.instanceOf();
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
