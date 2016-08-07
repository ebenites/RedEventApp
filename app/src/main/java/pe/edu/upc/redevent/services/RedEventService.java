package pe.edu.upc.redevent.services;

        import java.util.List;

        import pe.edu.upc.redevent.models.EventDetail;
        import pe.edu.upc.redevent.models.User;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.http.Field;
        import retrofit2.http.FormUrlEncoded;
        import retrofit2.http.Path;
        import retrofit2.http.GET;
        import retrofit2.http.POST;
        import retrofit2.http.PUT;

/**
 * Created by ebenites on 05/08/2016.
 */
public interface RedEventService {

    String API_BASE_URL = "https://eventos-ebenites.c9users.io";

    @FormUrlEncoded
    @POST("/api/login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/glogin")
    Call<User> glogin(@Field("email") String email, @Field("googleid") String googleid, @Field("fullname") String fullname);

    @FormUrlEncoded
    @POST("/api/users/{userid}/events/{eventid}")
    Call<EventDetail> event(@Path("userid") String userid, @Path("eventid") String eventid);

    @FormUrlEncoded
    @PUT("/api/users/{userid}/events/{eventid}")
    void checking(@Path("userid") String userid, @Path("eventid") String eventid);

    @FormUrlEncoded
    @PUT("/api/users/{userid}/events/{eventid}/{rating}")
    void rating(@Path("userid") String userid, @Path("eventid") String eventid, @Field("rating") Number rating);

}
