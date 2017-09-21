package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.util.Date;

/**
 *
 * @author Viseslav Sako
 */

public class QuickRoomReservationRequest
{
    private String nfccode;

    private Date startTime;

    private Date endTime;

    private String title;

    public QuickRoomReservationRequest(String nfccode, Date startTime, Date endTime, String title) {
        this.nfccode = nfccode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public String getNfccode() {
        return nfccode;
    }

    public void setNfccode(String nfccode) {
        this.nfccode = nfccode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
