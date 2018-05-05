package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView register;
    private EditText email, password;
    private FirebaseAuth mAuth;

    private final int REGISTER_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        this.mAuth = FirebaseAuth.getInstance();

        this.register = (TextView) findViewById(R.id.registerLink);
        this.email = (EditText) findViewById(R.id.email);
        this.password = (EditText) findViewById(R.id.password);

        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(intent, REGISTER_ACTIVITY);
            }
        });
    }

    public void login(View v){
        String emailText = email.getText().toString(),
               passwordText = password.getText().toString();

        if(emailText.equals("") || passwordText.equals("")){
            Toast.makeText(this, "Fill with credentials", Toast.LENGTH_SHORT).show();
        } else{
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent result = new Intent();
                                setResult(Activity.RESULT_OK, result);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_ACTIVITY && resultCode == Activity.RESULT_OK){
            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }
}
