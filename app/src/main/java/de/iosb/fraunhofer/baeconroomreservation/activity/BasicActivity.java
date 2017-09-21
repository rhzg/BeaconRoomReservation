package de.iosb.fraunhofer.baeconroomreservation.activity;

import android.support.v7.app.AppCompatActivity;

import de.iosb.fraunhofer.baeconroomreservation.entity.RoomDetailsRepresentation;

/**
 * This is an abstract clas that needs to be extendet from class that are showing details about rooms
 *
 * @author Viseslav Sako
 */

public abstract class BasicActivity extends AppCompatActivity
{
    public void setDetails(RoomDetailsRepresentation room){}
}
