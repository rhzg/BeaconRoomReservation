package iosb.fraunhofer.de.baeconroomreservation.rest;

import java.util.List;

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
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIterface
{
    @POST("/mobile/login")
    Call<LoginResponse> postLogin(@Body LoginRequest request);

    @POST("/mobile/room/{id}")
    Call<ReservationResponse> postReservation(@Path("id")String roomId, @Body ReserveRequest request);

    @POST("/mobile/nearby")
    Call<List<NerbyResponse>> postNerby(@Body NerbyRequest request);

    @GET("/mobile/users")
    Call<List<UserRepresentation>> getUsers();

    @GET("/mobile/room/favorite/{id}")
    Call<ReservationResponse> makeFavorite(@Path("id")String roomId);

    @GET("/mobile/favorites")
    Call<List<NerbyResponse>> getFavorite();

    @GET("/mobile/favorites/terms")
    Call<List<Term>> getFavoritesTerms();

    @GET("/mobile/users/terms")
    Call<List<Term>> getMyTerms();

    @POST("/mobile/favorites/terms/")
    Call<TermDetails> getSelectedTerm(@Body Term termId);

    @POST("/mobile/users/search")
    Call<List<UserRepresentation>> postSearchUsers(@Body SearchRequest query);

    @POST("/mobile/rooms/search")
    Call<List<UserRepresentation>> postSearchRooms(@Body SearchRequest query);

    @POST("/mobile/users/one")
    Call<UserDetailsRepresentation> getUserDetails(@Body SearchRequest query);

    @POST("/mobile/rooms/one")
    Call<RoomDetailsRepresentation> getRoomDetails(@Body SearchRequest searchRequest);
}
