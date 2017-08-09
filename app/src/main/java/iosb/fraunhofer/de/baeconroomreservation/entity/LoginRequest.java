package iosb.fraunhofer.de.baeconroomreservation.entity;

/**
 *
 * Created by sakovi on 08.08.2017.
 */

public class LoginRequest
{
    final String username;
    final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
