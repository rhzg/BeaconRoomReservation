package de.iosb.fraunhofer.baeconroomreservation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import de.iosb.fraunhofer.baeconroomreservation.auth.Authenticator;

/**
 *
 * @author Viselav Sako
 */

public class AuthenticatorService extends Service
{
    @Override
    public IBinder onBind(Intent intent)
    {
        Authenticator authenticator = new Authenticator(this);
        return authenticator.getIBinder();
    }
}