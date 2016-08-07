package pe.edu.upc.redevent.services;

import pe.edu.upc.redevent.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}
