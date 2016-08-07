package pe.edu.upc.redevent.models;

/**
 * Created by mchau on 07/08/2016.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventDetail {
    private String id;
    private String name;
    private String latitude;
    private String longitude;
    private String address;
    private String description;
    private Double price;
    private Date startdate;
    private Date enddate;
    private Number maxattendees;
    private String status;
    private Number registered;
    private String  image;

    public EventDetail() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Number getMaxattendees() {
        return maxattendees;
    }

    public void setMaxattendees(Number maxattendees) {
        this.maxattendees = maxattendees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Number getRegistered() {
        return registered;
    }

    public void setRegistered(Number registered) {
        this.registered = registered;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartDateConverted(){
        DateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        return ff.format(startdate);
    }

    @Override
    public String toString() {
        return "EventDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", maxattendees=" + maxattendees +
                ", status='" + status + '\'' +
                ", registered=" + registered +
                ", image='" + image + '\'' +
                '}';
    }

}
