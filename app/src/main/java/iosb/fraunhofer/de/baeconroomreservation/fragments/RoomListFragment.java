package iosb.fraunhofer.de.baeconroomreservation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import iosb.fraunhofer.de.baeconroomreservation.activity.RoomDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.adapters.NearbyArrayAdapter;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
import iosb.fraunhofer.de.baeconroomreservation.rest.Communicator;

public class RoomListFragment extends ListFragment implements BeaconConsumer
{
    private static final String TAG = "BeaconsEverywhere";
    BeaconManager beaconManager;
    public static NearbyArrayAdapter adapter;
    ArrayList<NerbyResponse> nearbyRoomses;
    private boolean favorites;

    public static RoomListFragment instanceOf(boolean favorites)
    {
        Bundle args = new Bundle();
        RoomListFragment frag = new RoomListFragment();
        args.putBoolean("favorites", favorites);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        favorites = getArguments().getBoolean("favorites");
        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        beaconManager.setForegroundScanPeriod(2000l);
        beaconManager.setForegroundBetweenScanPeriod(0l);
        BeaconManager.setAndroidLScanningDisabled(true);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        nearbyRoomses = new ArrayList<>();

        adapter = new NearbyArrayAdapter(getActivity(), nearbyRoomses);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(getApplicationContext(), RoomDetailsActivity.class);
        intent.putExtra("ROOM_ID", nearbyRoomses.get(position).getRoomID());
        intent.putExtra("ROOM_NAME", nearbyRoomses.get(position).getName());
        startActivity(intent);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        beaconManager.unbind(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        beaconManager.bind(this);
    }


    @Override
    public void onBeaconServiceConnect()
    {
        final Region region = new Region("Becons", Identifier.parse("F0018B9B-7509-4C31-A905-1A27D39C003C"), null, null);

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region)
            {
                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region)
            {
                try {
                    beaconManager.stopMonitoringBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {}
        });

        beaconManager.setRangeNotifier(new RangeNotifier()
        {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region)
            {
                if(!collection.isEmpty())
                {
                    if(favorites)
                    {
                        Communicator.favorites(collection);
                    } else
                        {
                        Communicator.nearbyPost(collection);
                    }
                }
                Log.d(TAG, "Time");
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection)
    {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i)
    {
        return getActivity().bindService(intent, serviceConnection, i);
    }
}
