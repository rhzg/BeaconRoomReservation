package de.iosb.fraunhofer.baeconroomreservation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.activity.RoomDetailsActivity;
import de.iosb.fraunhofer.baeconroomreservation.activity.UserDetailsActivity;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This is fragment which is used for search. It has two options for searching.
 * It can search for rooms and for users.
 *
 * @author Viseslav Sako
 */
public class SearchFragment extends BaseListFragment implements SearchView.OnQueryTextListener
{
    private ArrayAdapter<EntityRepresentation> adapter;
    private List<EntityRepresentation> entityRepresentations;
    @BindView(android.R.id.list) ListView _list;
    @BindView(R.id.searchview) SearchView _editsearch;
    @BindView(R.id.btnPeopleSearch) Button _btnPeopleSearch;
    @BindView(R.id.btnRoomsSearch) Button _btnRoomsSearch;

    //If false search for people if true search for rooms
    private boolean flag;

    public static SearchFragment instanceOf()
    {
        return new SearchFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, root);

        _editsearch.setOnQueryTextListener(this);

        entityRepresentations = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, entityRepresentations);
        _list.setAdapter(adapter);

        _btnPeopleSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flag = false;
                entityRepresentations.clear();
                adapter.notifyDataSetChanged();
                _btnPeopleSearch.setBackgroundResource(R.color.grey_600);
                _btnRoomsSearch.setBackgroundResource(R.color.grey_400);
            }
        });

        _btnRoomsSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flag = true;
                entityRepresentations.clear();
                adapter.notifyDataSetChanged();
                _btnRoomsSearch.setBackgroundResource(R.color.grey_600);
                _btnPeopleSearch.setBackgroundResource(R.color.grey_400);
            }
        });

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
            if(flag)
            {
                Communicator.searchRooms(newText, this);
            }
            else{
                Communicator.searchUsers(newText, this);
            }
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
        if(flag)
        {
            Intent intent = new Intent(getContext(), RoomDetailsActivity.class);
            intent.putExtra(Constants.roomID, entityRepresentations.get(position).getUserID());
            intent.putExtra(Constants.roomName, entityRepresentations.get(position).getName());
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getContext(), UserDetailsActivity.class);
            intent.putExtra(Constants.userID, entityRepresentations.get(position).getUserID());
            intent.putExtra(Constants.userName, entityRepresentations.get(position).getName());
            startActivity(intent);
        }
    }
}
