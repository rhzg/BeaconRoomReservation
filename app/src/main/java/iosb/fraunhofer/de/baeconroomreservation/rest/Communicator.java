package iosb.fraunhofer.de.baeconroomreservation.rest;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iosb.fraunhofer.de.baeconroomreservation.activity.BasicActivty;
import iosb.fraunhofer.de.baeconroomreservation.activity.MainActivity;
import iosb.fraunhofer.de.baeconroomreservation.activity.RoomDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.activity.RoomReservationActivity;
import iosb.fraunhofer.de.baeconroomreservation.activity.TermDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.activity.UserDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.adapters.UnixEpochDateTypeAdapter;
import iosb.fraunhofer.de.baeconroomreservation.auth.TokenAuthInterceptor;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.ReservationResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.ReserveRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.RoomDetailsRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.entity.SearchRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.Term;
import iosb.fraunhofer.de.baeconroomreservation.entity.TermDetails;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserDetailsRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.fragments.CalendarFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.PickRoomFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.RoomListFragment;
import iosb.fraunhofer.de.baeconroomreservation.fragments.SearchFragment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by sakovi on 08.08.2017.
 */

public class Communicator
{
    private static final String TAG = "Communicator";
    public static String token;
    public static boolean admin = false;
    private static APIterface service;
    private static Context context = null;
    private static final String AUTH_TOKEN_TYPE = "iosb.fraunhofer.de.baeconroomreservation";
    private static final String AUTH_TOKEN_TYPE_ACC_REG = "read_only";

    private static APIterface loginService;
    //TODO IP adrress
    private static final String SERVER_URL = "http://192.168.42.66";

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


        service = retrofit.create(APIterface.class);
    }

    private static void loginServiceInitalizator()
    {
        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (!TextUtils.isEmpty(token)) {
            TokenAuthInterceptor interceptor = new TokenAuthInterceptor(token);
            if (!httpClient.interceptors().contains(interceptor))
                httpClient.addInterceptor(interceptor);
        }

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        loginService = retrofit.create(APIterface.class);
    }

    public static boolean loginPost(String username, String password)
    {
        boolean returnValue = false;
        if(loginService == null) {loginServiceInitalizator();}
        service = null;
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

    public static List<NerbyResponse> nearbyPost(Collection<Beacon> becons)
    {
        if(service == null) {initalizator();}

        ArrayList<String> idList = new ArrayList<>();
        final List<NerbyResponse> nerbyResponse = new ArrayList<>();
        final Map<String, Beacon> beaconMap = new HashMap<>();
        for(Beacon beacon:becons)
        {
            beaconMap.put(beacon.getBluetoothName(), beacon);
            idList.add(beacon.getBluetoothName());
        }
        Call<List<NerbyResponse>> call = service.postNerby(new NerbyRequest(idList));

        call.enqueue(new Callback<List<NerbyResponse>>() {
            @Override
            public void onResponse(Call<List<NerbyResponse>> call, Response<List<NerbyResponse>> response)
            {
                if (response.code() == 200)
                {
                    nerbyResponse.addAll(response.body());
                    for(NerbyResponse response1: nerbyResponse)
                    {
                        Beacon beacon = beaconMap.get(response1.getRoomID());
                        response1.setDistance(beacon.getDistance());
                    }
                    RoomListFragment.adapter.setList((ArrayList<NerbyResponse>) nerbyResponse);
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }
            @Override
            public void onFailure(Call<List<NerbyResponse>> call, Throwable t)
            {
                Log.d(TAG, "problem");
            }
        });

        return nerbyResponse;
    }

    public static void roomReservation(String startTime, String endTime, String date, String roomId, String title,
                                          ArrayList<String> ids, final Activity activity)
    {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Reserving");
        progressDialog.setMessage("Trying to reserve...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ReserveRequest reserveRequest = new ReserveRequest(startTime, endTime, date, title, ids);
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

    public static void userGet(final RoomReservationActivity roomReservationActivity)
    {
        Call<List<UserRepresentation>> call = service.getUsers();

        call.enqueue(new Callback<List<UserRepresentation>>() {
            @Override
            public void onResponse(Call<List<UserRepresentation>> call, Response<List<UserRepresentation>> response)
            {
                if (response.code() == 200)
                {
                    roomReservationActivity.setUserRepresentations((ArrayList<UserRepresentation>) response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<List<UserRepresentation>> call, Throwable t) {

            }
        });
    }

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

    public static void makeFavorite(String roomId)
    {
        Call<ReservationResponse> call = service.makeFavorite(roomId);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if (response.code() == 200)
                {

                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                Log.d(TAG, "Favorite");
            }
        });
    }

    public static void favorites(Collection<Beacon> becons)
    {
        if(service == null) {initalizator();}

        ArrayList<String> idList = new ArrayList<>();
        final List<NerbyResponse> nerbyResponse = new ArrayList<>();
        final Map<String, Beacon> beaconMap = new HashMap<>();

        for(Beacon beacon:becons)
        {
            beaconMap.put(beacon.getBluetoothName(), beacon);
            idList.add(beacon.getBluetoothName());
        }
        Call<List<NerbyResponse>> call = service.getFavorite();

        call.enqueue(new Callback<List<NerbyResponse>>() {
            @Override
            public void onResponse(Call<List<NerbyResponse>> call, Response<List<NerbyResponse>> response)
            {
                if (response.code() == 200)
                {
                    nerbyResponse.addAll(response.body());
                    for(NerbyResponse response1: nerbyResponse)
                    {

                        Beacon beacon = beaconMap.get(response1.getRoomID());
                        if(beacon != null)
                        {
                            response1.setDistance(beacon.getDistance());
                        }
                    }
                    RoomListFragment.adapter.setList((ArrayList<NerbyResponse>) nerbyResponse);
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }
            @Override
            public void onFailure(Call<List<NerbyResponse>> call, Throwable t)
            {
                Log.d(TAG, "problem");
            }
        });
    }

    public static void getfavoritesTerms(final CalendarFragment fragment)
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

    public static void getMyTerms(final CalendarFragment fragment)
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

    public static void getTerm(Term term)
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
                    //TODO star new activity
                    Intent intent = new Intent(context, TermDetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("term", response.body()); //Your id
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

    public static void searchUsers(String query, final SearchFragment searchFragment)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(query);

        Call<List<UserRepresentation>> call = service.postSearchUsers(searchRequest);

        call.enqueue(new Callback<List<UserRepresentation>>() {
            @Override
            public void onResponse(Call<List<UserRepresentation>> call, Response<List<UserRepresentation>> response)
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
            public void onFailure(Call<List<UserRepresentation>> call, Throwable t) {

            }
        });
    }

    public static void searchRooms(String query, final SearchFragment searchFragment)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(query);

        Call<List<UserRepresentation>> call = service.postSearchRooms(searchRequest);

        call.enqueue(new Callback<List<UserRepresentation>>() {
            @Override
            public void onResponse(Call<List<UserRepresentation>> call, Response<List<UserRepresentation>> response)
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
            public void onFailure(Call<List<UserRepresentation>> call, Throwable t) {

            }
        });
    }

    public static void searchRooms(String query, final PickRoomFragment pickRoomFragment)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(query);

        Call<List<UserRepresentation>> call = service.postSearchRooms(searchRequest);

        call.enqueue(new Callback<List<UserRepresentation>>() {
            @Override
            public void onResponse(Call<List<UserRepresentation>> call, Response<List<UserRepresentation>> response)
            {
                if (response.code() == 200)
                {
                    pickRoomFragment.updateList(response.body());
                }
                else if(response.code() == 403)
                {
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<List<UserRepresentation>> call, Throwable t) {

            }
        });
    }

    public static void goToLogin()
    {
        Toast.makeText(context, "Token expired",
                Toast.LENGTH_SHORT).show();
        AccountManager am = AccountManager.get(context);
        am.invalidateAuthToken(AUTH_TOKEN_TYPE, token);
    }

    public static void setContext(Context context) {
        Communicator.context = context;
    }


    public static void getUserDetails(String user_id, final UserDetailsActivity activity)
    {
        if(service == null) {initalizator();}

        SearchRequest searchRequest = new SearchRequest(user_id);
        Call<UserDetailsRepresentation> call = service.getUserDetails(searchRequest);

        call.enqueue(new Callback<UserDetailsRepresentation>()
        {
            @Override
            public void onResponse(Call<UserDetailsRepresentation> call, Response<UserDetailsRepresentation> response)
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
            public void onFailure(Call<UserDetailsRepresentation> call, Throwable t) {

            }
        });
    }

    public static void getRoomDetails(String room_id, final BasicActivty activty)
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
                    activty.setDetails(response.body());
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
}
