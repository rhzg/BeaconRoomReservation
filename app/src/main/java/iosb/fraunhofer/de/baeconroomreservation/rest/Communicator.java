package iosb.fraunhofer.de.baeconroomreservation.rest;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import iosb.fraunhofer.de.baeconroomreservation.entity.LoginRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
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
    private static boolean returnValue = false;
    //TODO IP adrress
    private static final String SERVER_URL = "http://192.168.42.2";

    public static void initalizator()
    {
        //Here a logging interceptor is created
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //The Retrofit builder will have the client attached, in order to get connection logs
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URL)
                .build();

        service = retrofit.create(APIterface.class);
    }

    public static boolean loginPost(String username, String password)
    {
        if(service == null) {initalizator();}

        Call<LoginResponse> call = service.postLogin(new LoginRequest(username, password));

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

    public static void nearbyPost(Set<String> ids)
    {
        if(service == null) {initalizator();}

        ArrayList<String> idList = new ArrayList<>();
        idList.addAll(ids);
        Call<NerbyResponse> call = service.postNerby(new NerbyRequest(idList));

        call.enqueue(new Callback<NerbyResponse>() {
                @Override
                public void onResponse(Call<NerbyResponse> call, Response<NerbyResponse> response)
                {
                    Log.d(TAG, "Proslo");
                }

                @Override
                public void onFailure(Call<NerbyResponse> call, Throwable t)
                {
                    Log.d(TAG, "Nije");
                }
            }
        );

    }
}
