package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Manlio GR on 22/02/2018.
 */

public class IssueDetail extends AppCompatActivity {

    private ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private FirebaseUser user;
    private Query query;
    private IssueModel im;
    private String key,
                   boardID;
    private TextView name, description, status, points;

    private static final int EDIT_ISSUE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_detail);

        Intent intent = getIntent();
        this.mAuth = FirebaseAuth.getInstance();
        this.key = intent.getStringExtra("issue_key");
        this.boardID = intent.getStringExtra("board_key");
        this.user = mAuth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance().getReference();
        this.query = db.child("issues").child(key);

        name = (TextView) findViewById(R.id.nameIssue);
        description = (TextView) findViewById(R.id.descriptionIssue);
        status = (TextView) findViewById(R.id.statusIssue);
        points = (TextView) findViewById(R.id.storyPoints);

        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.wtf("name", dataSnapshot.getValue().toString());
                    HashMap map = (HashMap)dataSnapshot.getValue();
                    name.setText(map.get("name").toString());
                    description.setText(map.get("description").toString());
                    status.setText("Status: " + map.get("statusId").toString());
                    points.setText("Story Points:" + map.get("points").toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button boton = (Button) findViewById(R.id.editButton);

        Button boton2 = (Button) findViewById(R.id.deleteButton);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IssueDetail.this, EditIssue.class);
                intent.putExtra("issue_key", key);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, EDIT_ISSUE);
            }
        });

        boton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db.child("boards").child(boardID).child("issues").child(key).removeValue();
                db.child("issues").child(key).removeValue();
                Intent result = new Intent();
                setResult(7, result);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_ISSUE && resultCode == Activity.RESULT_OK){

            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }
}
