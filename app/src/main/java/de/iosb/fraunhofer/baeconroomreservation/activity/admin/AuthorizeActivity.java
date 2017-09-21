package de.iosb.fraunhofer.baeconroomreservation.activity.admin;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This is an activity which is used to authorize users. It uses NFC to scan to scan NFC tag and then
 * sends information for the resevation and NFC code to authorize the user.
 *
 * @author Viseslav Sako
 */

public class AuthorizeActivity extends AppCompatActivity
{
    private static final String TAG = "AuthorizeActivity";
    NfcAdapter mNfcAdapter;

    private Date startDate;
    private Date endDate;

    private String startTime;
    private String endTime;
    private String date;
    private String roomId;
    private String title;
    private ArrayList<String> ids;

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        startTime = getIntent().getStringExtra(Constants.startTime);
        endTime = getIntent().getStringExtra(Constants.endTime);

        startDate = (Date) getIntent().getSerializableExtra(Constants.startDate);
        endDate = (Date) getIntent().getSerializableExtra(Constants.endDate);

        date = getIntent().getStringExtra(Constants.date);
        title = getIntent().getStringExtra(Constants.title);
        roomId = getIntent().getStringExtra(Constants.roomID);
        ids = (ArrayList<String>) getIntent().getSerializableExtra(Constants.ids);


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            Log.i(TAG, payload.toString());
            setIntent(new Intent()); // Consume this intent.
        }
        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            getContent(msgs[0]);
        }
    }

    private void getContent(final NdefMessage msg)
    {
        String body = new String(msg.getRecords()[0].getPayload());
        if(startTime != null)
        {
            Communicator.roomReservation(body, startTime, endTime, date, roomId, title, ids, this);
        }else
        {
            Communicator.roomReservation(body, startDate, endDate, roomId, title, this);
        }
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    public void goBack() {
        Intent intent = new Intent(getApplicationContext(), RoomStatusActivity.class);
        intent.putExtra(Constants.roomName, getIntent().getStringExtra(Constants.roomName));
        intent.putExtra(Constants.roomID, roomId);
        startActivity(intent);
    }
}
