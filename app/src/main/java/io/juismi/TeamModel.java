package io.juismi;

import java.util.List;

/**
 * Created by JARVIS on 11/04/2018.
 */

public class TeamModel {

    private String name;
    private String boardID;

    public TeamModel(){
        this.name = null;
        this.boardID = null;
    }

    public TeamModel(String name, String boardID){
        this.name = name;
        this.boardID = boardID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
