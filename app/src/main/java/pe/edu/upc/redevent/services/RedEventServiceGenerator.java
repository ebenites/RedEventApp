package pe.edu.upc.redevent.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ebenites on 06/08/2016.
 * Best Practice: RedEventServiceGenerator https://futurestud.io/blog/retrofit-getting-started-and-android-client#servicegenerator
 */
public final class RedEventServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(RedEventService.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static RedEventService redEventService = null;

    private static Retrofit retrofit;

    private RedEventServiceGenerator() {
    }

    public static RedEventService createService() {

        if(redEventService == null){
            // Retrofit Debug: https://futurestud.io/blog/retrofit-2-log-requests-and-responses
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            retrofit = builder.client(httpClient.build()).build();

            redEventService = retrofit.create(RedEventService.class);
        }

        return redEventService;
    }

    public static Retrofit retrofit(){
        if(retrofit == null)
            createService();
        return retrofit;
    }

}
