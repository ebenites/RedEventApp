package pe.edu.upc.redevent.models;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import retrofit2.Converter;
import retrofit2.Response;
/**
 * Created by mchau on 07/08/2016.
 */
public class APISuccess {
    private int status;

    private String message;

    public APISuccess() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "APISuccess{message='" + message + '\'' +'}';

    }




}
