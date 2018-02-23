package io.juismi;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ismael on 18/02/2018.
 */

public class IssueModel {
    String name;
    String description;
    String status;
    String tag;
    String comment;
    String AssignedTo;
    int points;
    //TagModel tag;
    //ArrayList<CommentModel> commments;
    //String AssignedTo;
    //Date creationDate;
    //int points;

    String noComments;
    String id;

    public IssueModel(){}
  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAssignedTo() {
        return AssignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        AssignedTo = assignedTo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getNoComments() {
        return noComments;
    }

    public void setNoComments(String noComments) {
        this.noComments = noComments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IssueModel(String name, String noComments, int status){
        this.name = name;
        this.noComments = noComments;
        this.setStatus(status);
        this.id = id;
    }

    public IssueModel(String name, String description, String tag, String status, String comment, String assignedTo, int points) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.tag = tag;
        this.comment = comment;
        this.AssignedTo = assignedTo;
        this.points = points;
    }

    public void setStatus(int status){
        if(status == 0){
            this.status = "Status: To Do";
        }
        if(status == 1){
            this.status = "Status: Doing";
        }
        if(status == 2){
            this.status = "Status: Done";
        }
    }


}
