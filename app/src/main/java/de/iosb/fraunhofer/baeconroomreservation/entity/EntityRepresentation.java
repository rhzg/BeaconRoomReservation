package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.io.Serializable;

/**
 *
 * @author Viseslav Sako
 */

public class EntityRepresentation implements Serializable
{
    String name;

    String userID;

    public EntityRepresentation(String name, String userID) {
        this.name = name;
        this.userID = userID;
    }

    public EntityRepresentation() {
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
