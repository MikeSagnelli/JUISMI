package io.juismi;

/**
 * Created by JARVIS on 13/04/2018.
 */

public class UserModel {

    private String name;
    private String email;

    public UserModel(){
        this.name = null;
        this.email = null;
    }

    public UserModel(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
