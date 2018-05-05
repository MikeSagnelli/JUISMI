package io.juismi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IssueModel {

    private String name,
                   description,
                   status,
                   boardID,
                   userID,
                   dueDate;

    private int priority;

    public IssueModel(){
        this.name = null;
        this.description = null;
        this.priority = 0;
        this.status = null;
        this.boardID = null;
        this.userID = null;
        this.dueDate = null;
    }

    public IssueModel(String name, String description, int points, String status, String boardID, String userID, String date){
        this.name = name;
        this.description = description;
        this.priority = points;
        this.status = status;
        this.boardID = boardID;
        this.userID = userID;
        this.dueDate = date;

    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName(){return this.name;}

    public String getDescription() {return this.description;}

    public int getPoints(){return this.priority;}

    public String getStatusId(){return this.status;}

    public void setName(String name){this.name = name;}

    public void setDescription(String description){this.description = description;}

    public void setPoints(int points){this.priority = points;}

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

    public String getUserID(){ return this.userID; }

    public void  setUserID(String userID){ this.userID = userID; }

}
