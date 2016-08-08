package pe.edu.upc.redevent.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    private String name;
    private String description;
    private String category;
    private String latitude;
    private String longitude;
    private String address;
    private String startdate;
    private int maxattendees;
    private String status;
    private String image;

    private Topics topics;

    public Event(String name, String category, String startdate){
        super();
        this.name = name;
        this.category = category;
        this.startdate = startdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public int getMaxattendees() {
        return maxattendees;
    }

    public void setMaxattendees(int maxattendees) {
        this.maxattendees = maxattendees;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    public Topics getTopics() {
        return topics;
    }
}
