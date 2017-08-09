package iosb.fraunhofer.de.baeconroomreservation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import iosb.fraunhofer.de.baeconroomreservation.auth.Authenticator;

/**
 *
 * Created by sakovi on 08.08.2017.
 */

public class AuthenticatorService extends Service
{
    // Instance field that stores the authenticator object
    // Notice, this is the same Authenticator class we defined earlier
    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}