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

    public NearbyRoom(double distance, String id) {
        this.distance = distance;
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBLEid() {
        return id;
    }

    public void setBLEid(String BLEid) {
        this.id = BLEid;
    }
}
