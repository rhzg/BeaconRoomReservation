package de.iosb.fraunhofer.baeconroomreservation.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.RoomReservationActivity;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;
import de.iosb.fraunhofer.baeconroomreservation.views.PickUserView;


/**
 * This is fragment that shows all users and  you can chose which you want to invite to date.
 *
 * @author Viseslav Sako
 */
public class PickUserFragment extends DialogFragment implements SearchView.OnQueryTextListener, BaseListFragment
{
    private static final String USERS = "users";

    @BindView(android.R.id.list) ListView _list;
    @BindView(R.id.searchview) SearchView _editsearch;
    @BindView(R.id.btnCencel) Button _btnCencel;
    @BindView(R.id.btnOK) Button _btnOK;
    @BindView(R.id.addUsers) LinearLayout _addUsers;
    private List<EntityRepresentation> entityRepresentations;
    private List<EntityRepresentation> returnToActivity;
    private ArrayAdapter<EntityRepresentation> adapter;

    private RoomReservationActivity activity;

    public void setActivity(RoomReservationActivity activity) {
        this.activity = activity;
    }


    public static PickUserFragment newInstance(ArrayList<EntityRepresentation> entityRepresentations)
    {
        PickUserFragment frag = new PickUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(USERS, entityRepresentations);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pick_user, container, false);
        ButterKnife.bind(this, view);

        _editsearch.setOnQueryTextListener(this);
        entityRepresentations = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, entityRepresentations);
        _list.setAdapter(adapter);

        returnToActivity = (ArrayList<EntityRepresentation>) getArguments().getSerializable(USERS);

        for (EntityRepresentation entityRepresentation : returnToActivity)
        {
            PickUserView pickUserView = new PickUserView(this);
            pickUserView.setUsername(entityRepresentation.getUserID());
            pickUserView.setName(entityRepresentation.getName());
            _addUsers.addView(pickUserView);
        }

        _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!returnToActivity.contains(entityRepresentations.get(position)))
                {
                    PickUserView pickUserView = new PickUserView(PickUserFragment.this);
                    pickUserView.setUsername(entityRepresentations.get(position).getUserID());
                    pickUserView.setName(entityRepresentations.get(position).getName());
                    _addUsers.addView(pickUserView);
                    returnToActivity.add(entityRepresentations.get(position));
                }
            }
        });

        _btnCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        _btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setUserText();
                dismiss();
            }
        });
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if(newText.length()>2)
        {
            Communicator.searchUsers(newText, this);
            entityRepresentations.clear();
            adapter.notifyDataSetChanged();
        }
        else
        {
            entityRepresentations.clear();
            adapter.notifyDataSetChanged();
        }
        return  false;
    }

    @Override
    public void updateList(List<EntityRepresentation> body)
    {
        entityRepresentations.clear();
        entityRepresentations.addAll(body);
        adapter.notifyDataSetChanged();
    }

    public void deleteUser(String username)
    {
        for (Iterator<EntityRepresentation> iterator = returnToActivity.iterator(); iterator.hasNext();) {
            EntityRepresentation entityRepresentation = iterator.next();
            if (entityRepresentation.getUserID().equals(username))
            {
                iterator.remove();
            }
        }
    }
}
