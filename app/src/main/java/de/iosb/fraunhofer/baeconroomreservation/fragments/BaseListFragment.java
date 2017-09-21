package de.iosb.fraunhofer.baeconroomreservation.fragments;

import android.support.v4.app.ListFragment;

import java.util.List;

import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;

/**
 * This is class the will bee extended by all list fragments in this application.
 * it has one method that must be implanted.
 * Created by Viseslav Sako
 */

public abstract class BaseListFragment extends ListFragment
{

    /**
     * This method is called when the list view must be update with new elements.
     * @param body list of new elements.
     */
    public void updateList(List<EntityRepresentation> body) {}
}
