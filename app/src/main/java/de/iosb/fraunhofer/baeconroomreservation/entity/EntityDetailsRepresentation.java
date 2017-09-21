package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Viseslav Sako
 */

public class EntityDetailsRepresentation extends EntityRepresentation
{
    String email;

    String phoneNumber;

    List<Term> terms = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
}
