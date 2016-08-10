package pe.edu.upc.redevent.services;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pe.edu.upc.redevent.models.APIMessage;

import pe.edu.upc.redevent.models.APISuccess;

import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.models.Topic;
import pe.edu.upc.redevent.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.PUT;
import retrofit2.http.Part;

import retrofit2.http.Path;

/**
 * Created by ebenites on 05/08/2016.
 */
public interface RedEventService {

    String API_BASE_URL = "https://eventos-ebenites.c9users.io";

    @FormUrlEncoded
    @POST("/api/login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @Multipart
    @POST("/api/glogin")
    Call<User> glogin(@Part("email") RequestBody email, @Part("token") RequestBody token, @Part("googleid") RequestBody googleid, @Part("fullname") RequestBody fullname, @Part MultipartBody.Part photo);

    @Multipart
    @POST("/api/users/{id}/photo")
    Call<APIMessage> upload_photo(@Path("id") long userId, @Part MultipartBody.Part photo);

    @GET("/api/users/{id}/events")
    Call<List<Event>> getEvents(@Path("id") long userId);

    @GET("/api/users/{id}/myevents")
    Call<List<Event>> getMyEvents(@Path("id") long userId);

    @POST("/api/users/{id}/topics")
    Call<APIMessage> savePreferences(@Path("id") long userId, @Body List<Long> topics);

    @GET("/api/topics")
    Call<List<Topic>> getTopics();

    @PUT("/api/users/{userid}/events/{eventid}")
    Call<APISuccess> checking(@Path("userid") String userid, @Path("eventid") String eventid);

    @PUT("/api/users/{userid}/events/{eventid}/{rating}")
    Call<APISuccess> rating(@Path("userid") String userid, @Path("eventid") String eventid, @Path("rating") Integer rating);

    @POST("/api/users/{userid}/events/{eventid}")
    Call<String> joinEvent(@Path("userid") String userid, @Path("eventid") String eventid );

    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") long userid);

}
