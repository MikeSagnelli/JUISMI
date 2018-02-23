package io.juismi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueModel {

    private String name,
                   description,
                   status;
    private int points;
    private List<Integer> tagIDs;

    public IssueModel(){
        this.name = null;
        this.description = null;
        this.points = 0;
        this.status = null;
        this.tagIDs = new ArrayList<Integer>();
    }

    public IssueModel(String name, String description, int points, String status, List<Integer> tagIDs){
        this.name = name;
        this.description = description;
        this.points = points;
        this.status = status;
        this.tagIDs = tagIDs;
    }

    public String getName(){return this.name;}

    public String getDescription() {return this.description;}

    public int getPoints(){return this.points;}

    public String getStatusId(){return this.status;}

    private List<Integer> getTagIDs(){return this.tagIDs;}

    public void setName(String name){this.name = name;}

    public void setDescription(String description){this.description = description;}

    public void setPoints(int points){this.points = points;}

    public void setStatusId(String status){this.status = status;}

    public void setTagIDs(List<Integer> tagIDs){this.tagIDs = tagIDs;}

    public void addInt(int a){
        this.tagIDs.add(a);
    }
}
