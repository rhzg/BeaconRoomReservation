package de.iosb.fraunhofer.baeconroomreservation.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Viseslav sako
 */

public class TermDetails extends Term implements Serializable
{
    private EntityRepresentation initializer;

    private List<EntityRepresentation> users;

    public TermDetails(){}

        public TermDetails(Date startDate, Date endDate, String location, String description, String roomId, EntityRepresentation initializer, List<EntityRepresentation> users) {
        super(startDate, endDate, location, description, roomId);
        this.initializer = initializer;
        this.users = users;
    }

    public EntityRepresentation getInitializer() {
        return initializer;
    }

    public void setInitializer(EntityRepresentation initializer) {
        this.initializer = initializer;
    }

    public List<EntityRepresentation> getUsers() {
        return users;
    }

    public void setUsers(List<EntityRepresentation> users) {
        this.users = users;
    }
}
