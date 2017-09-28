package de.iosb.fraunhofer.baeconroomreservation.rest;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.altbeacon.beacon.Beacon;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.activity.BasicActivity;
import de.iosb.fraunhofer.baeconroomreservation.activity.RoomReservationActivity;
import de.iosb.fraunhofer.baeconroomreservation.activity.TermDetailsActivity;
import de.iosb.fraunhofer.baeconroomreservation.activity.UserDetailsActivity;
import de.iosb.fraunhofer.baeconroomreservation.activity.admin.AuthorizeActivity;
import de.iosb.fraunhofer.baeconroomreservation.adapters.UnixEpochDateTypeAdapter;
import de.iosb.fraunhofer.baeconroomreservation.auth.TokenAuthInterceptor;
import de.iosb.fraunhofer.baeconroomreservation.entity.LoginRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.LoginResponse;
import de.iosb.fraunhofer.baeconroomreservation.entity.NearbyRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.NearbyRoom;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomOverview;
import de.iosb.fraunhofer.baeconroomreservation.entity.QuickRoomReservationRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.ReservationResponse;
import de.iosb.fraunhofer.baeconroomreservation.entity.ReservationRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.RoomDetailsRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.entity.SearchRequest;
import de.iosb.fraunhofer.baeconroomreservation.entity.Term;
import de.iosb.fraunhofer.baeconroomreservation.entity.TermDetails;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityDetailsRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.entity.EntityRepresentation;
import de.iosb.fraunhofer.baeconroomreservation.fragments.BaseListFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.CalendarFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.RoomListFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.SearchFragment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This calls build retrofit service and makes calls tu the server and reacts on the results of the calls.
 *
 * @author Viseslav Sako
 */
public class Communicator
{
    private static final String TAG = "Communicator";

    public static String token;
    public static boolean admin = false;

    private static Context context = null;
    private static final String AUTH_TOKEN_TYPE = "iosb.fraunhofer.de.baeconroomreservation";

    private static APIInterface service;
    private static APIInterface loginService;
    //URL root url for making calls
    private static final String SERVER_URL = "http://192.168.42.211";

    /**
     * Initialize the Retrofit service for making network calls.
     */
    private static void initalizator()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (!TextUtils.isEmpty(token)) {
            TokenAuthInterceptor interceptor = new TokenAuthInterceptor(token);
            if (!httpClient.interceptors().contains(interceptor))
                httpClient.addInterceptor(interceptor);
        }

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(SERVER_URL)
                .build();


        service = retrofit.create(APIInterface.class);
    }

    /**
     * Initialize the Retrofit service for making the login call.
     */
    private static void loginServiceInitalizator()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        loginService = retrofit.create(APIInterface.class);
    }

    /**
     *
     * @param username that is used for login
     * @param password password for login
     * @return         returns boolean value, true if login succeed and false if not
     */
    public static boolean loginPost(String username, String password)
    {
        boolean returnValue = false;

        if(loginService == null)
        {
            loginServiceInitalizator();
        }

        service = null;

//        String passHash = createHashPassword(password);

        Call<LoginResponse> call = loginService.postLogin(new LoginRequest(username, password));
        try {
            Response<LoginResponse> loginResponse = call.execute();
            if(loginResponse.code() == 200)
            {
                token = loginResponse.body().getToken();
                admin = loginResponse.body().isAdmin();
                Log.e(TAG,"Success");
                returnValue = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }


    /**
     * Get the list of all nearby rooms
     * @param beacons    collection of beacons that where found with scanning
     * @return  list of all nearby rooms
     */
    //TODO distance
    public static List<RoomOverview> nearbyPost(Collection<Beacon> beacons)
    {
        if(service == null) {initalizator();}

        ArrayList<NearbyRoom> nearbyRooms = new ArrayList<>();
        final List<RoomOverview> roomOverview = new ArrayList<>();
        final Map<String, Beacon> beaconMap = new HashMap<>();

        for(Beacon beacon:beacons)
        {
            beaconMap.put(beacon.getBluetoothName(), beacon);
            nearbyRooms.add(new NearbyRoom(beacon.getDistance() ,beacon.getBluetoothName()));
        }

        Call<List<RoomOverview>> call = service.postNerby(new NearbyRequest(nearbyRooms));

        call.enqueue(new Callback<List<RoomOverview>>() {
            @Override
            public void onResponse(Call<List<RoomOverview>> call, Response<List<RoomOverview>> response)
            {
                if (response.code() == 200)
                {
                    roomOverview.addAll(response.body());
                    for(RoomOverview response1: roomOverview)
                    {
                        Beacon beacon = beaconMap.get(response1.getBleId());
                        response1.setDistance(beacon.getDistance());
                    }
                    RoomListFragment.adapter.setList((ArrayList<RoomOverview>) roomOverview);
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }
            @Override
            public void onFailure(Call<List<RoomOverview>> call, Throwable t)
            {
                Log.d(TAG, "problem");
            }
        });
        return roomOverview;
    }

    /**
     * This is a method which is used to make reservation for users when they are logged in and are using
     * there phone to make the reservation.
     *
     * @param startTime is the time when date begins
     * @param endTime   is the time when date ends
     * @param date      is the date when the date is happening
     * @param roomId    is the id of the room in which the date will take place
     * @param title     some sort of description of the date
     * @param ids       id of people how are invited to the date
     * @param activity  is the activity where call was made
     */
    public static void roomReservation(String startTime, String endTime, String date, String roomId, String title, ArrayList<String> ids, final Activity activity)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Reserving");
        progressDialog.setMessage("Trying to reserve...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ReservationRequest reservationRequest = new ReservationRequest(startTime, endTime, date, title, ids);
        Call<ReservationResponse> call = service.postReservation(roomId, reservationRequest);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if (response.code() == 200)
                {
                    Toast.makeText(activity, "Reservation was success: "+response.body().isSuccess(),
                            Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                progressDialog.hide();
                Toast.makeText(context, "Ups something went wrong",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "problem");
            }
        });

    }

    /**
     * This is a method for making reservation when you are using status display and you need to authenticate yourself with NFC.
     *
     * @param NFC       is the NFC code which user used to authenticate himself
     * @param startTime is the time when date begins
     * @param endTime   is the time when date ends
     * @param date      is the date when the date is happening
     * @param roomId    is the id of the room in which the date will take place
     * @param title     some sort of description of the date
     * @param ids       id of people how are invited to the date
     * @param activity  is the activity where call was made
     */
    public static void roomReservation(String NFC, String startTime, String endTime, String date, final String roomId, String title, ArrayList<String> ids, final AuthorizeActivity activity)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Reserving");
        progressDialog.setMessage("Trying to reserve...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ReservationRequest reservationRequest = new ReservationRequest(NFC ,startTime, endTime, date, title, ids);
        Call<ReservationResponse> call = service.postReservation(roomId, reservationRequest);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if (response.code() == 200)
                {
                    Toast.makeText(activity, "Reservation was success: "+response.body().isSuccess(),
                            Toast.LENGTH_SHORT).show();

                    activity.goBack();
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                progressDialog.hide();
                Toast.makeText(context, "Ups something went wrong",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "problem");
            }
        });
    }

    /**
     * This method is used when we are making quick reservation.
     *
     * @param NFC       is the NFC code which user used to authenticate himself
     * @param startDate time and date when the date begins
     * @param endDate   time and date when the date ends
     * @param roomId    is the id of the room in which the date will take place
     * @param title     some sort of description of the date
     * @param activity  is the activity where call was made
     */
    public static void roomReservation(String NFC, Date startDate, Date endDate, String roomId, String title, final Activity activity)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Reserving");
        progressDialog.setMessage("Trying to reserve...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        QuickRoomReservationRequest reserveRequest = new QuickRoomReservationRequest(NFC ,startDate, endDate, title);
        Call<ReservationResponse> call = service.postReservation(roomId, reserveRequest);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if (response.code() == 200)
                {
                    Toast.makeText(activity, "Reservation was success: "+response.body().isSuccess(),
                            Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                progressDialog.hide();
                Toast.makeText(context, "Ups something went wrong",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "problem");
            }
        });
    }

    /**
     * This method is used for getting all users.
     * @param roomReservationActivity activity where the call was made and where the list of user needs to be sent.
     */
//    public static void usersGet(final RoomReservationActivity roomReservationActivity)
//    {
//        Call<List<EntityRepresentation>> call = service.getUsers();
//
//        call.enqueue(new Callback<List<EntityRepresentation>>() {
//            @Override
//            public void onResponse(Call<List<EntityRepresentation>> call, Response<List<EntityRepresentation>> response)
//            {
//                if (response.code() == 200)
//                {
//                    roomReservationActivity.setEntityRepresentations((ArrayList<EntityRepresentation>) response.body());
//                }
//                else if(response.code() == 403)
//                {
//                    goToLogin();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<EntityRepresentation>> call, Throwable t)
//            {
//
//            }
//        });
//    }

    /**
     * Method for making the call for adding room to favorites or unfavorites.
     * @param roomId    id of the room which status is changing
     * @param activity  activity where the call was made
     */
    public static void makeFavorite(String roomId, final Activity activity)
    {
        Call<ReservationResponse> call = service.makeFavorite(roomId);

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Trying");
        progressDialog.setMessage("Trying to make room favorite/unfavorite.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if (response.code() == 200)
                {
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                progressDialog.dismiss();
                Toast.makeText(context, "Ups something went wrong", Toast.LENGTH_SHORT).show();            }
        });
    }

    /**
     * Method for making the call for adding room to favorites or unfavorites.
     * @param roomId    id of the room which status is changing
     * @param context   context for showing dialog
     */
    public static void makeFavorite(String roomId, final Context context)
    {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Trying");
        progressDialog.setMessage("Trying to make room favorite/unfavorite.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<ReservationResponse> call = service.makeFavorite(roomId);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if(response.code() == 403)
                {
                    goToLogin();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                progressDialog.dismiss();
                Toast.makeText(context, "Ups something went wrong", Toast.LENGTH_SHORT).show();                   }
        });
    }

    /**
     *  Method for getting favorite rooms.
     * @param beacons  nearby beacons
     */
    public static void favorites(Collection<Beacon> beacons)
    {
        if(service == null) {initalizator();}

        final List<RoomOverview> roomOverview = new ArrayList<>();
        final Map<String, Beacon> beaconMap = new HashMap<>();

        for(Beacon beacon:beacons)
        {
            beaconMap.put(beacon.getBluetoothName(), beacon);
        }
        Call<List<RoomOverview>> call = service.getFavorite();

        call.enqueue(new Callback<List<RoomOverview>>() {
            @Override
            public void onResponse(Call<List<RoomOverview>> call, Response<List<RoomOverview>> response)
            {
                if (response.code() == 200)
                {
                    roomOverview.addAll(response.body());
                    for(RoomOverview response1: roomOverview)
                    {
                        double distance = 1000;
                        for (String bleId : response1.getBleIds())
                        {
                            Beacon beacon = beaconMap.get(bleId);
                            if(beacon != null && (beacon.getDistance() < distance))
                            {
                                distance = beacon.getDistance();
                                response1.setDistance(beacon.getDistance());
                            }
                        }
                    }
                    RoomListFragment.adapter.setList((ArrayList<RoomOverview>) roomOverview);
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }
            @Override
            public void onFailure(Call<List<RoomOverview>> call, Throwable t)
            {
                Log.d(TAG, "problem");
            }
        });
    }

    /**
     * Method for getting dates in favorite rooms.
     * @param fragment  is fragment where the dates are shown
     */
    public static void getFavoriteDates(final CalendarFragment fragment)
    {
        if(service == null) {initalizator();}

        Call<List<Term>> call = service.getFavoritesTerms();

        call.enqueue(new Callback<List<Term>>() {
            @Override
            public void onResponse(Call<List<Term>> call, Response<List<Term>> response)
            {
                if (response.code() == 200)
                {
                    fragment.setTerms(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<List<Term>> call, Throwable t)
            {
                fragment.fail();
            }
        });
    }

    /**
     * Method for getting dates of current user.
     * @param fragment  is fragment where the dates are shown
     */
    public static void getMyDates(final CalendarFragment fragment)
    {
        if(service == null) {initalizator();}

        Call<List<Term>> call = service.getMyTerms();

        call.enqueue(new Callback<List<Term>>() {
            @Override
            public void onResponse(Call<List<Term>> call, Response<List<Term>> response)
            {
                if (response.code() == 200)
                {
                    fragment.setTerms(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<List<Term>> call, Throwable t)
            {
                fragment.fail();
            }
        });
    }

    /**
     * Method for getting date details.
     * @param term The date for which we are getting details
     */
    public static void getTermDetails(Term term)
    {
        Call<TermDetails> call = service.getSelectedTerm(term);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Getting");
        progressDialog.setMessage("Trying to get term...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        call.enqueue(new Callback<TermDetails>()
        {
            @Override
            public void onResponse(Call<TermDetails> call, Response<TermDetails> response)
            {
                if (response.code() == 200)
                {
                    Intent intent = new Intent(context, TermDetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.term, response.body()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    context.startActivity(intent);
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<TermDetails> call, Throwable t)
            {
                progressDialog.dismiss();
                Toast.makeText(context, "Ups something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method for posting search query for users.
     * @param query pattern which is used for searching
     * @param searchFragment fragment where the results are shown
     */
    public static void searchUsers(String query, final BaseListFragment searchFragment)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(query);

        Call<List<EntityRepresentation>> call = service.postSearchUsers(searchRequest);

        call.enqueue(new Callback<List<EntityRepresentation>>() {
            @Override
            public void onResponse(Call<List<EntityRepresentation>> call, Response<List<EntityRepresentation>> response)
            {
                if (response.code() == 200)
                {
                    searchFragment.updateList(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<List<EntityRepresentation>> call, Throwable t) {

            }
        });
    }

    /**
     * Method for posting search query for rooms.
     * @param query pattern which is used for searching
     * @param searchFragment fragment where the results are shown
     */
    public static void searchRooms(String query, final BaseListFragment searchFragment)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(query);

        Call<List<EntityRepresentation>> call = service.postSearchRooms(searchRequest);

        call.enqueue(new Callback<List<EntityRepresentation>>() {
            @Override
            public void onResponse(Call<List<EntityRepresentation>> call, Response<List<EntityRepresentation>> response)
            {
                if (response.code() == 200)
                {
                    searchFragment.updateList(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<List<EntityRepresentation>> call, Throwable t) {

            }
        });
    }

    /**
     * Method for getting user details
     * @param user_id id of user whose details are we getting
     * @param activity activity where the details are shown
     */
    public static void getUserDetails(String user_id, final UserDetailsActivity activity)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(user_id);
        Call<EntityDetailsRepresentation> call = service.getUserDetails(searchRequest);

        call.enqueue(new Callback<EntityDetailsRepresentation>()
        {
            @Override
            public void onResponse(Call<EntityDetailsRepresentation> call, Response<EntityDetailsRepresentation> response)
            {
                if (response.code() == 200)
                {
                    activity.setDetails(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<EntityDetailsRepresentation> call, Throwable t) {

            }
        });
    }

    /**
     * Method for getting room details
     * @param room_id of room which detail we are getting
     * @param activity activity where the details are shown
     */
    public static void getRoomDetails(String room_id, final BasicActivity activity)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(room_id);
        Call<RoomDetailsRepresentation> call = service.getRoomDetails(searchRequest);

        call.enqueue(new Callback<RoomDetailsRepresentation>()
        {
            @Override
            public void onResponse(Call<RoomDetailsRepresentation> call, Response<RoomDetailsRepresentation> response)
            {
                if (response.code() == 200)
                {
                    activity.setDetails(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<RoomDetailsRepresentation> call, Throwable t) {

            }
        });
    }


    /**
     * Method for managing 403 response. Invalidating token.
     */
    private static void goToLogin()
    {
        Toast.makeText(context, "Token expired",
                Toast.LENGTH_SHORT).show();
        AccountManager am = AccountManager.get(context);
        am.invalidateAuthToken(AUTH_TOKEN_TYPE, token);
    }

    public static void setContext(Context context) {
        Communicator.context = context;
    }
}
