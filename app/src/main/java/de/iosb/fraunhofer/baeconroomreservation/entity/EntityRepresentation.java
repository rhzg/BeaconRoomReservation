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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityRepresentation that = (EntityRepresentation) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return userID != null ? userID.equals(that.userID) : that.userID == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
    }
}
