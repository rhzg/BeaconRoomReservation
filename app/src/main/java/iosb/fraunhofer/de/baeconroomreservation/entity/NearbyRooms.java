package iosb.fraunhofer.de.baeconroomreservation.entity;

/**
 *
 * Created by sakovi on 09.08.2017.
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
