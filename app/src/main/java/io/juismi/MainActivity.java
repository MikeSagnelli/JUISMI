package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView register;
    private Button login;

    private static final int REGISTER_ACTIVITY = 0;
    private static final int LOGIN_ACTIVITY = 1;
    private static final int ISSUES_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mAuth = FirebaseAuth.getInstance();
        this.mAuth.signOut();
        this.checkSession();

        this.register = (TextView) findViewById(R.id.register);
        this.login = (Button) findViewById(R.id.login);

        this.register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                requestRegister();
            }
        });

        this.login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                requestLogin();
            }
        });
    }

    public void checkSession(){
        FirebaseUser currentUser = this.mAuth.getCurrentUser();

        if(currentUser != null){
            Intent intent = new Intent(this, IssuesActivity.class);
            intent.putExtra("userName", currentUser.getDisplayName());
            startActivityForResult(intent, ISSUES_ACTIVITY);
        }
    }

    public void requestLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY);
    }

    public void requestRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_ACTIVITY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_ACTIVITY && resultCode == Activity.RESULT_OK){
            this.checkSession();
            Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == LOGIN_ACTIVITY && resultCode == Activity.RESULT_OK){
            this.checkSession();
            Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            this.mAuth.signOut();
            this.checkSession();
        }
    }
}