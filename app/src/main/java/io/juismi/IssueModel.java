package io.juismi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueModel {

    private String name,
                   description,
                   status,
                    statusId,
                   boardID;
    private int points;

    public IssueModel(){
        this.name = null;
        this.description = null;
        this.points = 0;
        this.status = null;
        this.boardID = null;
    }

    public IssueModel(String name, String description, int points, String status, String boardID){
        this.name = name;
        this.description = description;
        this.points = points;
        this.status = status;
        this.boardID = boardID;
    }

    public String getName(){return this.name;}

    public String getDescription() {return this.description;}

    public int getPoints(){return this.points;}

    public String getStatusId(){return this.status;}

    public void setName(String name){this.name = name;}

    public void setDescription(String description){this.description = description;}

    public void setPoints(int points){this.points = points;}

    public void setStatusId(String status){this.status = status;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

}
