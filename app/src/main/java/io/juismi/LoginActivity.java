package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView register;
    private Button login;
    private EditText email, password;
    private final int REGISTER_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        register = (TextView) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(intent, REGISTER_CODE);
            }
        });
    }

    public void login(View v){
        String emailText = email.getText().toString(),
                passwordText = password.getText().toString();
        if(emailText.equals("") || passwordText.equals("")){
            Toast.makeText(this, "Llena los campos", Toast.LENGTH_SHORT).show();
        } else{
            // API CALL
        }
    }


}
