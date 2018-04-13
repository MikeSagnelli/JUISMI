package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
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
    private FirebaseAdapter adapter;
    private ArrayList<String> tags;

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
        listView = (ListView) findViewById(R.id.tagLstView);

        this.query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.wtf("name", dataSnapshot.getValue().toString());
                    HashMap map = (HashMap)dataSnapshot.getValue();
                    name.setText(map.get("name").toString());
                    description.setText(map.get("description").toString());
                    status.setText("Status: " + map.get("status").toString());
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
                intent.putStringArrayListExtra("tags", tags);
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

        this.setTags();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_ISSUE && resultCode == Activity.RESULT_OK){

            Intent result = new Intent();
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }

    private void setTags(){
        this.tags = new ArrayList<>();
        this.adapter = new FirebaseAdapter<TagModel>(this.db.child("tags").orderByChild("issues/"+this.key).equalTo(true), TagModel.class,R.layout.detail_tag_row, this) {
            @Override
            protected void populateView(View v, TagModel model) {
                RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.rowLayout);
                int index = getModels().indexOf(model);
                tags.add(getKey(index));
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                layout.setBackgroundColor(Color.parseColor(hexColor));
            }
        };

        this.listView.setAdapter(this.adapter);

    }

}
