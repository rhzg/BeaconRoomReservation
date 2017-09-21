package de.iosb.fraunhofer.baeconroomreservation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.activity.RoomReservationActivity;
import de.iosb.fraunhofer.baeconroomreservation.adapters.NearbyArrayAdapter;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomOverview;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This fragment contains BLE beacon manager. It listens for beacons for defined period of time
 * and shows nearby rooms or favorite rooms and the distance from the user to the room.
 *
 * @author Viselsav Sako
 */
public class RoomListFragment extends ListFragment implements BeaconConsumer
{
    private static final String TAG = "BeaconsEverywhere";
    private static final String FAVORITE = "FAVORITE";
    private static final String BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static long SCAN_PERIOD = 2000;
    private static long BETWEEN_SCAN_PERIOD = 0;

    BeaconManager beaconManager;
    public static NearbyArrayAdapter adapter;
    ArrayList<RoomOverview> nearbyRooms;
    private boolean favorite;

    /**
     * This method is used for instancing {@link RoomListFragment}.
     *
     * @param favorite boolean parametar which detarmins is this for nearby or favorite rooms
     * @return  instance of this class
     */
    public static RoomListFragment instanceOf(boolean favorite)
    {
        Bundle args = new Bundle();
        RoomListFragment frag = new RoomListFragment();
        args.putBoolean(FAVORITE, favorite);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        favorite = getArguments().getBoolean(FAVORITE);

        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        beaconManager.setForegroundScanPeriod(SCAN_PERIOD);
        beaconManager.setForegroundBetweenScanPeriod(BETWEEN_SCAN_PERIOD);
        BeaconManager.setAndroidLScanningDisabled(true);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_LAYOUT));
        beaconManager.bind(this);

        nearbyRooms = new ArrayList<>();
        adapter = new NearbyArrayAdapter(getActivity(), nearbyRooms);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(getApplicationContext(), RoomReservationActivity.class);
        intent.putExtra(Constants.roomID, nearbyRooms.get(position).getRoomID());
        intent.putExtra(Constants.roomName, nearbyRooms.get(position).getName());
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
//        final Region region = new Region("Becons", Identifier.parse("F0018B9B-7509-4C31-A905-1A27D39C003C"), null, null);
        final Region region = new Region(TAG, null, null, null);

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
                    if(favorite)
                    {
                        Communicator.favorites(collection);
                    } else
                        {
                        Communicator.nearbyPost(collection);
                    }
                }
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
