package io.juismi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class CollaboratorDetails extends AppCompatActivity {

    private String userID,
                   boardID;

    private TextView name,
                     email;

    private Query query;

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator_details);

        this.userID = getIntent().getStringExtra("user_key");
        this.boardID = getIntent().getStringExtra("board_key");

        this.db = FirebaseDatabase.getInstance().getReference();

        this.query = this.db.child("users").child(this.userID);

        this.name = findViewById(R.id.name);
        this.email = findViewById(R.id.email);

        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.wtf("name", dataSnapshot.getValue().toString());
                    HashMap map = (HashMap)dataSnapshot.getValue();
                    name.setText(map.get("name").toString());
                    email.setText(map.get("email").toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button delete = findViewById(R.id.deleteButton);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.child("users").child(userID).child("boards").child(boardID).removeValue();
                db.child("boards").child(boardID).child("team").child(userID).removeValue();
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            }
        });
        getSupportActionBar().setTitle("Collaborator Details");
    }
}
