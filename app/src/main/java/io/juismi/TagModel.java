package io.juismi;

/**
 * Created by Ismael on 22/02/2018.
 */

public class TagModel {
    String color;
    String name;
    String id;

    public TagModel(String color, String name, String id){
        this.color = color;
        this.name = name;
        this.id = this.id;

    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {

        return color;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
