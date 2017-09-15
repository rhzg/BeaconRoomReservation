package iosb.fraunhofer.de.baeconroomreservation.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import butterknife.BindView;
import iosb.fraunhofer.de.baeconroomreservation.R;
import iosb.fraunhofer.de.baeconroomreservation.activity.admin.RoomStatusActivity;

/**
 *
 * Created by sakovi on 15.09.2017.
 */

public class QuickPickFragment extends DialogFragment
{
    public static final String TAG = "NfcDemo";

    @BindView(R.id.one) Button _one;
    @BindView(R.id.two) Button _two;
    @BindView(R.id.three) Button _three;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;

    public static QuickPickFragment newInstance(String roomID) {
        QuickPickFragment frag = new QuickPickFragment();
        Bundle args = new Bundle();
        args.putString("ROOM_ID", roomID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quick_pick_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());

        mNfcPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        _one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });
        String roomID = getArguments().getString("ROOM_ID");
    }



}
