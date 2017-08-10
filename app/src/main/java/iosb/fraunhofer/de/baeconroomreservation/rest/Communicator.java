package iosb.fraunhofer.de.baeconroomreservation.rest;

import android.text.TextUtils;
import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import iosb.fraunhofer.de.baeconroomreservation.auth.TokenAuthInterceptor;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
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
    private static boolean returnValue = false;
    //TODO IP adrress
    private static final String SERVER_URL = "http://192.168.42.2";

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
}
