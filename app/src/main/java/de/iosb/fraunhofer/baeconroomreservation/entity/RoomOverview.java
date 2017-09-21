package de.iosb.fraunhofer.baeconroomreservation.entity;


/**
 *
 * @author Viseslav Sako
 */

public class RoomOverview
{
    private String roomID;

    private Boolean occupied;

    private Boolean favorite;

    private double distance;

    private String from;

    private String until;

    private String name;

    public RoomOverview(String roomID, Boolean occupied, String from, String until, String name) {
        this.roomID = roomID;
        this.occupied = occupied;
        this.from = from;
        this.until = until;
        this.name = name;
    }

    public RoomOverview() {
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

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
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
