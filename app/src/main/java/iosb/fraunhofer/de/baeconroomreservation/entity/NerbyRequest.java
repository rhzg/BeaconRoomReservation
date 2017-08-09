package iosb.fraunhofer.de.baeconroomreservation.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * Created by sakovi on 09.08.2017.
 */

public class NerbyRequest
{
    @SerializedName("ids")
    @Expose
    private ArrayList<String> ids;

    public NerbyRequest(ArrayList<String> ids) {
        this.ids = ids;
    }
}
