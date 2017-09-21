package de.iosb.fraunhofer.baeconroomreservation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import de.iosb.fraunhofer.baeconroomreservation.activity.RoomReservationActivity;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;


/**
 * This is fragment that shows all users and  you can chose which you want to invite to date.
 *
 * @author Viseslav Sako
 */
public class CheckBoxDialogFragment extends DialogFragment
{
    private static final String CHECKED_ITEMS = "checkedItems";
    private static final String USERS = "users";

    private RoomReservationActivity activity;

    public void setActivity(RoomReservationActivity activity) {
        this.activity = activity;
    }


    /**
     * This method is for instancing this fragment. It creats a fragment that has list of
     * all users and check box checked if they are selected.
     *
     * @param checkedItems array which position of true elements is the same as the position of selected users in entityRepresentations
     * @param entityRepresentations array of users
     * @return instane of this class
     */
    public static CheckBoxDialogFragment newInstance(boolean[] checkedItems, ArrayList<EntityRepresentation> entityRepresentations)
    {
        CheckBoxDialogFragment frag = new CheckBoxDialogFragment();
        Bundle args = new Bundle();
        args.putBooleanArray(CHECKED_ITEMS, checkedItems);
        args.putSerializable(USERS, entityRepresentations);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final boolean[] checkedItems = getArguments().getBooleanArray(CHECKED_ITEMS);
        ArrayList<EntityRepresentation> entityRepresentations = (ArrayList<EntityRepresentation>) getArguments().getSerializable(USERS);
        final CharSequence[] usernames = new CharSequence[entityRepresentations.size()];

        for (int i = 0; i< entityRepresentations.size(); i++)
        {
            usernames[i] = entityRepresentations.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add users")
                .setMultiChoiceItems(usernames, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                checkedItems[which] = isChecked;
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        activity.setUserText();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });

         return builder.create();
    }
}
