package iosb.fraunhofer.de.baeconroomreservation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.activity.admin.RoomStatusActivity;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;


public class PickRoomFragment extends ListFragment implements SearchView.OnQueryTextListener
{
    private ArrayAdapter<UserRepresentation> adapter;
    private List<UserRepresentation> userRepresentations;
    @BindView(android.R.id.list) ListView _list;
    @BindView(R.id.searchview) SearchView _editsearch;

    public static PickRoomFragment instanceOf()
    {
        return new PickRoomFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.pick_room_fragment, container, false);
        ButterKnife.bind(this, root);
        _editsearch.setOnQueryTextListener(this);

        userRepresentations = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userRepresentations);
        _list.setAdapter(adapter);

        return root;
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
            Communicator.searchRooms(newText, this);
        }
        else
        {
            userRepresentations.clear();
            adapter.notifyDataSetChanged();
        }
        return  false;
    }

    public void updateList(List<UserRepresentation> body)
    {
        userRepresentations.clear();
        userRepresentations.addAll(body);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(getContext(), RoomStatusActivity.class);
        intent.putExtra("ROOM_ID", userRepresentations.get(position).getUserID());
        intent.putExtra("ROOM_NAME", userRepresentations.get(position).getName());
        startActivity(intent);
    }
}
