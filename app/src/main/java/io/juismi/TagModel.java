package io.juismi;

/**
 * Created by Ismael on 22/02/2018.
 */

public class TagModel {
    private String color;
    private String name;
    private int id;

    public TagModel(String color, String name, int id){
        this.color = color;
        this.name = name;
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() { return color;}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
