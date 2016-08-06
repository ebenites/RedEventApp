package pe.edu.upc.redevent.models;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ebenites on 06/08/2016.
 */
public class APIError {

    private int status;

    private String message;

    public APIError() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "APIError{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public static APIError parseError(Retrofit retrofit, Response<?> response) {
        Converter<ResponseBody, APIError> converter = retrofit.responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }
        return error;
    }

}
