package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.util.ArrayList;

/**
 *
 *  Created by sakovi on 14.08.2017.
 */

public class ReservationRequest
{
    private String startTime;

    private String endTime;

    private String nfccode;

    private String date;

    private String title;

    private ArrayList<String> users;

    public ReservationRequest(String startTime, String endTime, String date, String title, ArrayList<String> users) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.title = title;
        this.users = users;
    }

    public ReservationRequest(String nfccode, String startTime, String endTime, String date, String title, ArrayList<String> users) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.title = title;
        this.users = users;
        this.nfccode = nfccode;
    }

    public ReservationRequest() {
    }

    public String getNfccode() {
        return nfccode;
    }

    public void setNfccode(String nfccode) {
        this.nfccode = nfccode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}

