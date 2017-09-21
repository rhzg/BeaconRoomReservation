package de.iosb.fraunhofer.baeconroomreservation.entity;

/**
 *
 * @author Viseslav Sako
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
