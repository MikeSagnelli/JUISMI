package io.juismi;

/**
 * Created by Ismael on 22/02/2018.
 */

public class TagModel {
    private int color;
    private String name;

    public TagModel(){
        this.color = 0;
        this.name = null;
    }

    public TagModel(int color, String name){
        this.color = color;
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getColor() { return color;}

    public String getName() {
        return name;
    }

}
