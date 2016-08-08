package pe.edu.upc.redevent.models;

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
