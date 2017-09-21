package de.iosb.fraunhofer.baeconroomreservation.entity;

/**
 *
 * @author Viseslav Sako
 */

public class NearbyRooms
{
    private double distance;

    private String BLEid;

    public NearbyRooms(double distance, String BLEid) {
        this.distance = distance;
        this.BLEid = BLEid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBLEid() {
        return BLEid;
    }

    public void setBLEid(String BLEid) {
        this.BLEid = BLEid;
    }
}
