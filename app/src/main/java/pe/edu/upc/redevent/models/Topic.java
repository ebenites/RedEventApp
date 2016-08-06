package pe.edu.upc.redevent.models;

import com.orm.dsl.Table;

/**
 * Created by ebenites on 05/08/2016.
 */
@Table
public class Topic{

    private Long id;

    private String name;

    public Topic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
