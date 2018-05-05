package io.juismi;

import org.w3c.dom.Comment;

/**
 * Created by JARVIS on 05/05/2018.
 */

public class CommentModel {

    private String comment, issueID;

    public CommentModel(){
        this.comment = null;
        this.issueID = null;
    }

    public CommentModel(String comment, String issueID){
        this.comment = comment;
        this.issueID = issueID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }
}
