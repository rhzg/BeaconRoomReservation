package iosb.fraunhofer.de.baeconroomreservation.rest;


import java.util.List;

import iosb.fraunhofer.de.baeconroomreservation.entity.LoginRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.LoginResponse;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyRequest;
import iosb.fraunhofer.de.baeconroomreservation.entity.NerbyResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIterface
{
    @POST("/mobile/login")
    Call<LoginResponse> postLogin(@Body LoginRequest request);

    @POST("/mobile/nerby")
    Call<NerbyResponse> postNerby(@Body NerbyRequest request);
}
