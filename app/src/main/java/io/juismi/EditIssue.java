package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditIssue extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private Spinner status;
    private String key;
    private TextView name, description, points;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_edit);

        this.status = (Spinner) findViewById(R.id.status_input2);
        this.name = (TextView) findViewById(R.id.name_input2);
        this.description = (TextView) findViewById(R.id.description_input2);
        this.points = (TextView) findViewById(R.id.points_input2);

        Intent intent = getIntent();
        this.key = intent.getStringExtra("issue_key");

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.status.setAdapter(adapter);

        Map<String, Object> map = new HashMap<String,Object>();
        Query query = db.child(this.user.getUid()).child("issues").child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.wtf("name", dataSnapshot.getValue().toString());
                    HashMap map = (HashMap)dataSnapshot.getValue();
                    name.setText(map.get("name").toString());
                    description.setText(map.get("description").toString());
                    switch(map.get("statusId").toString()){
                        case "To Do":
                            status.setSelection(0);
                            break;
                        case "Doing":
                            status.setSelection(1);
                            break;
                        case "Done":
                            status.setSelection(2);
                            break;
                    }
                    points.setText(map.get("points").toString());
                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveButtonClicked(View v){

        Map<String, Object> postValues = new HashMap<String,Object>();
        postValues.put("name", ((EditText) findViewById(R.id.name_input2)).getText().toString());
        postValues.put("description", ((EditText) findViewById(R.id.description_input2)).getText().toString());
        postValues.put("statusId", ((Spinner) findViewById(R.id.status_input2)).getSelectedItem().toString());
        postValues.put("points", Integer.parseInt(((EditText) findViewById(R.id.points_input2)).getText().toString()));

        mDatabase.child(user.getUid()).child("issues").child(this.key).updateChildren(postValues);

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
