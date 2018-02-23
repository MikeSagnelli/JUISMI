package io.juismi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int REGISTER_ACTIVITY = 0;
    private static final int LOGIN_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mAuth = FirebaseAuth.getInstance();

        boolean sessionExists = this.checkUser();
    }

    public boolean checkUser(){
        FirebaseUser currentUser = this.mAuth.getCurrentUser();
        return (currentUser != null);
    }

    public void requestLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY);
    }

    public void requestRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_ACTIVITY);
    }
}