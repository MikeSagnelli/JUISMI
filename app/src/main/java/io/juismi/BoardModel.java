package io.juismi;

import java.util.List;

/**
 * Created by JARVIS on 11/04/2018.
 */

public class BoardModel {

    private String name;
    private String ownerID;

    public BoardModel(){
        this.name = null;
        this.ownerID = null;
    }

    public BoardModel(String name, String ownerID){
        this.name = name;
        this.ownerID = ownerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}
