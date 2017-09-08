package iosb.fraunhofer.de.baeconroomreservation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.activity.RoomDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.activity.UserDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;


public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener
{
    private ArrayAdapter<UserRepresentation> adapter;
    private List<UserRepresentation> userRepresentations;
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
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, root);
        _editsearch.setOnQueryTextListener(this);

        userRepresentations = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userRepresentations);
        _list.setAdapter(adapter);

        _btnPeopleSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flag = false;
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
        if(flag)
        {
            Intent intent = new Intent(getContext(), RoomDetailsActivity.class);
            intent.putExtra("ROOM_ID", userRepresentations.get(position).getUserID());
            intent.putExtra("ROOM_NAME", userRepresentations.get(position).getName());
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getContext(), UserDetailsActivity.class);
            intent.putExtra("USER_ID", userRepresentations.get(position).getUserID());
            intent.putExtra("USER_NAME", userRepresentations.get(position).getName());
            startActivity(intent);
        }
    }
}
