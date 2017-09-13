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
    @Override
    public IBinder onBind(Intent intent) {

        Authenticator authenticator = new Authenticator(this);
        return authenticator.getIBinder();
    }
}