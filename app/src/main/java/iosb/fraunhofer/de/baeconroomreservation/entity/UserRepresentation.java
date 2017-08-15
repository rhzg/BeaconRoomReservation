package iosb.fraunhofer.de.baeconroomreservation.entity;

/**
 *
 * Created by sakovi on 15.08.2017.
 */

public class UserRepresentation
{
    private String name;

    private String userID;

    public UserRepresentation(String name, String userID) {
        this.name = name;
        this.userID = userID;
    }

    public UserRepresentation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return name;
    }
}
