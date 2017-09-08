package iosb.fraunhofer.de.baeconroomreservation.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakovi on 08.09.2017.
 */

public class RoomDetailsRepresentation
{
    private boolean favorite;

    private boolean occupied;

    private List<Term> terms = new ArrayList<>();

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
}
