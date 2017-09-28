package de.iosb.fraunhofer.baeconroomreservation.fragments;

import java.util.List;

import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;

/**
 * This is class the will bee extended by all list fragments in this application.
 * it has one method that must be implanted.
 * Created by Viseslav Sako
 */

public interface BaseListFragment
{

    /**
     * This method is called when the list view must be update with new elements.
     * @param body list of new elements.
     */
     void updateList(List<EntityRepresentation> body);
}
