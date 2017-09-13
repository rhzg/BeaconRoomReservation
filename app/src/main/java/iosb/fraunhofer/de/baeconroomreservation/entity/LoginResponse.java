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

    @SerializedName("admin")
    private boolean admin;

    public LoginResponse(String token, boolean admin) {
        this.token = token;
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
