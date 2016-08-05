package pe.edu.upc.redevent.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private String name;
    private String description;
    private String category;
    private Date dateStart;

    public Event(String name, String category, Date dateStart){
        super();
        this.name = name;
        this.category = category;
        this.dateStart = dateStart;
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateStartConverted(){
        DateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        return ff.format(dateStart);
    }
}
