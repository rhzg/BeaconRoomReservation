package iosb.fraunhofer.de.baeconroomreservation.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by sakovi on 24.08.2017.
 */

public class TermDetails extends Term implements Serializable
{
    private UserRepresentation initilaizer;

    private List<UserRepresentation> users;

    public TermDetails(){}

        public TermDetails(Date startDate, Date endDate, String location, String description, String roomId, UserRepresentation initilaizer, List<UserRepresentation> users) {
        super(startDate, endDate, location, description, roomId);
        this.initilaizer = initilaizer;
        this.users = users;
    }

    public UserRepresentation getInitilaizer() {
        return initilaizer;
    }

    public void setInitilaizer(UserRepresentation initilaizer) {
        this.initilaizer = initilaizer;
    }

    public List<UserRepresentation> getUsers() {
        return users;
    }

    public void setUsers(List<UserRepresentation> users) {
        this.users = users;
    }
}
