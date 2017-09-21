package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Viseslav Sako
 */

public class RoomDetailsRepresentation
{
    private boolean favorite;

    private boolean occupied;

    private List<Term> terms = new ArrayList<>();

    private Date until;

    public RoomDetailsRepresentation(){}

    public RoomDetailsRepresentation(boolean favorite, boolean occupied, List<Term> terms) {
        this.favorite = favorite;
        this.occupied = occupied;
        this.terms = terms;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }
}
