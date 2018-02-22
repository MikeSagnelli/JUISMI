package io.juismi;

/**
 * Created by Ismael on 18/02/2018.
 */

public class IssueModel {
    String name;
    String noComments;
    String status;

    public IssueModel(String name, String noComments, String status){
        this.name = name;
        this.noComments = noComments;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getNoComments() {
        return noComments;
    }

    public String getStatus() {
        return status;
    }
}
