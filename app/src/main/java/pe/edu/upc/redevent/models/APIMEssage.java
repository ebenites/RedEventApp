package pe.edu.upc.redevent.models;

/**
 * Created by ebenites on 08/08/2016.
 */
public class APIMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "APIMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
