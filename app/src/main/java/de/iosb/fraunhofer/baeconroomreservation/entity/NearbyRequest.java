package de.iosb.fraunhofer.baeconroomreservation.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * @author Viseslav Sako
 */

public class NearbyRequest
{
    @SerializedName("ids")
    @Expose
    private ArrayList<String> ids;

    public NearbyRequest(ArrayList<String> ids) {
        this.ids = ids;
    }
}
