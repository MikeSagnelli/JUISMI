package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterIssue extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Spinner status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_issue);

        this.status = (Spinner) findViewById(R.id.status_input);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.status.setAdapter(adapter);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveButtonClicked(View v){

        IssueModel newIssue = new IssueModel(
                ((EditText) findViewById(R.id.name_input)).getText().toString(),
                ((EditText) findViewById(R.id.description_input)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.points_input)).getText().toString()),
                ((Spinner) findViewById(R.id.status_input)).getSelectedItem().toString(),
                new ArrayList<Integer>()
        );
        newIssue.addInt(5);

        mDatabase.child(user.getUid()).child("issues").push().setValue(newIssue);

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
