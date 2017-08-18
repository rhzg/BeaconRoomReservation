package iosb.fraunhofer.de.baeconroomreservation.entity;


/**
 *
 * Created by sakovi on 09.08.2017.
 */

public class NerbyResponse
{
    private String roomID;

    private Boolean occupied;

    private Boolean favorite;

    private double distance;

    private String from;

    private String untill;

    private String name;

    public NerbyResponse(String roomID, Boolean occupied, String from, String untill, String name) {
        this.roomID = roomID;
        this.occupied = occupied;
        this.from = from;
        this.untill = untill;
        this.name = name;
    }

    public NerbyResponse() {
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUntill() {
        return untill;
    }

    public void setUntill(String untill) {
        this.untill = untill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
