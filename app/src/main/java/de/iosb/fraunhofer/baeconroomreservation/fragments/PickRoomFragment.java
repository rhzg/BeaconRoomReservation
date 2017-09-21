package de.iosb.fraunhofer.baeconroomreservation.fragments;


import android.content.Intent;
import android.os.Bundle;
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
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.admin.RoomStatusActivity;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This class is responsible for fragment that provides a search through rooms.
 * On click on one of rooms on list it assigned this device to this room.
 *
 * @author Viseslav Sako
 */
public class PickRoomFragment extends BaseListFragment implements SearchView.OnQueryTextListener
{
    private ArrayAdapter<EntityRepresentation> adapter;
    private List<EntityRepresentation> entityRepresentations;
    @BindView(android.R.id.list) ListView _list;
    @BindView(R.id.searchview) SearchView _editsearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.pick_room_fragment, container, false);
        ButterKnife.bind(this, root);
        _editsearch.setOnQueryTextListener(this);

        entityRepresentations = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, entityRepresentations);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(getContext(), RoomStatusActivity.class);
        intent.putExtra(Constants.roomID, entityRepresentations.get(position).getUserID());
        intent.putExtra(Constants.roomName, entityRepresentations.get(position).getName());
        startActivity(intent);
    }
}
