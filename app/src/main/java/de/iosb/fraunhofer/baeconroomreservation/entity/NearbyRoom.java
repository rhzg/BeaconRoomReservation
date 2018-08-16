package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.io.Serializable;

/**
 *
 * @author Viseslav Sako
 */
public class NearbyRoom implements Serializable
{
    private double distance;

    private String id;
    private String proximityUUID;
    private String minor;
    private String major;

    public NearbyRoom(double distance, String id, String uuid, String major, String minor) {
        this.distance = distance;
        this.id = id;
        this.proximityUUID = uuid;
        this.major = major;
        this.minor = minor;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public String getMinor() {
        return minor;
    }

    public String getMajor() {
        return major;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMajor(String id) {
        this.major = id;
    }

    public void setMinor(String id) {
        this.minor = id;
    }

    public String getProximityUUID() {
        return proximityUUID;
    }

    public void setProximityUUID(String proximityUUID) {
        this.proximityUUID = proximityUUID;
    }

}
