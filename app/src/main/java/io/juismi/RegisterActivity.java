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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView login;
    private EditText email, name, password, passwordConfirm;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
                        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        this.mAuth = FirebaseAuth.getInstance();

        this.login = (TextView) findViewById(R.id.loginLink);
        this.email = (EditText) findViewById(R.id.email);
        this.name = (EditText) findViewById(R.id.newTagName);
        this.password = (EditText) findViewById(R.id.password);
        this.passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);



        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                setResult(Activity.RESULT_CANCELED, result);
                Toast.makeText(RegisterActivity.this, "Returned to log in.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public void submit(View v){
        String emailText = email.getText().toString().trim(),
                passwordText = password.getText().toString().trim(),
                nameText = name.getText().toString(),
                passwordConfirmText = passwordConfirm.getText().toString().trim();

        boolean notFilled = emailText.equals("") || passwordText.equals("") || nameText.equals("") || passwordConfirmText.equals("");
        if(notFilled){
            Toast.makeText(this, "Fill with credentials", Toast.LENGTH_SHORT).show();

        } else if(!validate(emailText)){
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();

        } else if(passwordText.length() < 6){
            Toast.makeText(this, "Minimum 6 characters in password", Toast.LENGTH_SHORT).show();

        } else if(!passwordText.equals(passwordConfirmText)){
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();

        }else{
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            } else {

                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user != null) {
                                    db.child("users").child(user.getUid()).child("name").setValue(name.getText().toString());
                                }
                                Intent result = new Intent();
                                setResult(Activity.RESULT_OK, result);
                                finish();
                            }
                        }
                    });
        }

    }


}
