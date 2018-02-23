package io.juismi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView login;
    private Button submit;
    private EditText email, name, password, passwordConfirm;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        //auth = FirebaseAuth.getInstance();

        login = (TextView) findViewById(R.id.login);
        submit = (Button) findViewById(R.id.submit);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
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
            Toast.makeText(this, "Llena los campos", Toast.LENGTH_SHORT).show();

        } else if(!validate(emailText)){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();

        } else if(passwordText.length() < 6){
            Toast.makeText(this, "Contraseña mínimo 6 caracteres", Toast.LENGTH_SHORT).show();

        } else if(!passwordText.equals(passwordConfirmText)){
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();

        }else{
            /*auth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Error de registro", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent result = new Intent();
                                setResult(Activity.RESULT_OK, result);
                                finish();
                            }
                        }
                    });*/

        }

    }


}
