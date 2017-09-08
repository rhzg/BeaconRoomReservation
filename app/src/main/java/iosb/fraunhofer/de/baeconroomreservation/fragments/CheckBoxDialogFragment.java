package iosb.fraunhofer.de.baeconroomreservation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import iosb.fraunhofer.de.baeconroomreservation.activity.RoomReservationActivity;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;



public class CheckBoxDialogFragment extends DialogFragment
{
    private RoomReservationActivity activity;

    public void setActivity(RoomReservationActivity activity) {
        this.activity = activity;
    }

    public static CheckBoxDialogFragment newInstance(boolean[] checkedItems, ArrayList<UserRepresentation> userRepresentations)
    {
        CheckBoxDialogFragment frag = new CheckBoxDialogFragment();
        Bundle args = new Bundle();
        args.putBooleanArray("checkedItems", checkedItems);
        args.putSerializable("users", userRepresentations);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final boolean[] checkedItems = getArguments().getBooleanArray("checkedItems");
        ArrayList<UserRepresentation> userRepresentations = (ArrayList<UserRepresentation>) getArguments().getSerializable("users");
        final CharSequence[] usernames = new CharSequence[userRepresentations.size()];

        for (int i = 0; i<userRepresentations.size(); i++)
        {
            usernames[i] = userRepresentations.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Add users")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(usernames, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                checkedItems[which] = isChecked;
                            }
                        })
                // Set the action buttons
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
