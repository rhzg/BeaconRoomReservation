package de.iosb.fraunhofer.baeconroomreservation.entity;


import java.util.HashSet;
import java.util.Set;

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

    private String bleId;

    private Set<String> bleIds = new HashSet<>();

    public RoomOverview(String roomID, Boolean occupied, Boolean favorite, double distance, String from, String until, String name, String bleId) {
        this.roomID = roomID;
        this.occupied = occupied;
        this.favorite = favorite;
        this.distance = distance;
        this.from = from;
        this.until = until;
        this.name = name;
        this.bleId = bleId;
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

    public String getBleId() {
        return bleId;
    }

    public void setBleId(String bleId) {
        this.bleId = bleId;
    }

    public Set<String> getBleIds() {
        return bleIds;
    }

    public void setBleIds(Set<String> bleIds) {
        this.bleIds = bleIds;
    }
}