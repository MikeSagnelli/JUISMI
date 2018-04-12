package io.juismi;

/**
 * Created by Ismael on 22/02/2018.
 */

public class TagModel {
    private int color;
    private String name;
    private String boardID;

    public TagModel(){
        this.color = 0;
        this.name = null;
    }

    public TagModel(int color, String name, String board){
        this.color = color;
        this.name = name;
        this.boardID = board;
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
