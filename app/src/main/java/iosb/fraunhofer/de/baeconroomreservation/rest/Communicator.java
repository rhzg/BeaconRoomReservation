package iosb.fraunhofer.de.baeconroomreservation.rest;

import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.altbeacon.beacon.Beacon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iosb.fraunhofer.de.baeconroomreservation.activity.RoomDetailsActivity;
import iosb.fraunhofer.de.baeconroomreservation.auth.TokenAuthInterceptor;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.ReservationResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.ReserveRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.UserRepresentation;
import iosb.fraunhofer.de.baeconroomreservation.fragments.RoomListFragment;
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
    private static APIterface service;
    private static APIterface loginService;
    //TODO IP adrress
    private static final String SERVER_URL = "http://192.168.42.189";

    private static void initalizator()
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
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

        Call<LoginResponse> call = loginService.postLogin(new LoginRequest(username, password));

        try {
            Response<LoginResponse> loginResponse = call.execute();
            if(loginResponse.code() == 200)
            {
                token = loginResponse.body().getToken();
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
            }
            @Override
            public void onFailure(Call<List<NerbyResponse>> call, Throwable t)
            {
                Log.d(TAG, "problem");
            }
        });

        return nerbyResponse;
    }

    public static boolean roomReservation(String startTime, String endTime, String date, String roomId, String title, ArrayList<String> ids)
    {
        final boolean[] returnValue = new boolean[1];
        ReserveRequest reserveRequest = new ReserveRequest(startTime, endTime, date, title, ids);
        Call<ReservationResponse> call = service.postReservation(roomId, reserveRequest);

        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response)
            {
                if (response.code() == 200)
                {
                    returnValue[0] = true;
                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t)
            {
                Log.d(TAG, "problem");
            }
        });

        return returnValue[0];
    }

    public static void userGet(final RoomDetailsActivity roomDetailsActivity)
    {
        Call<List<UserRepresentation>> call = service.getUsers();

        call.enqueue(new Callback<List<UserRepresentation>>() {
            @Override
            public void onResponse(Call<List<UserRepresentation>> call, Response<List<UserRepresentation>> response)
            {
                if (response.code() == 200)
                {
                    roomDetailsActivity.setUserRepresentations((ArrayList<UserRepresentation>) response.body());
                }
            }

            @Override
            public void onFailure(Call<List<UserRepresentation>> call, Throwable t) {

            }
        });
    }
}
