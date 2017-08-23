package iosb.fraunhofer.de.baeconroomreservation.entity;

/**
 *
 * Created by sakovi on 23.08.2017.
 */

public class Term
{
    private String startDate;

    private String endDate;

    private String location;

    private String description;

    public Term() {
    }

    public Term(String startDate, String endDate, String location, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
