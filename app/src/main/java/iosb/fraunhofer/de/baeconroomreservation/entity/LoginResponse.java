package iosb.fraunhofer.de.baeconroomreservation.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * Created by sakovi on 08.08.2017.
 */

public class LoginResponse implements Serializable
{
    @SerializedName("token")
    private String token;

    public LoginResponse(String token)
    {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
