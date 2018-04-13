package io.juismi;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CollaboratorsActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton addCollab;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private FirebaseAdapter adapter;
    private String boardID;

    private static final int REGISTER_COLLAB = 0;
    private static final int DETAILS_COLLAB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborators);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.boardID = getIntent().getStringExtra("board_key");

        this.listView = findViewById(R.id.listCollaborators);
        this.addCollab = findViewById(R.id.addCollaborators);

        this.addCollab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(CollaboratorsActivity.this, RegisterCollaborator.class);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, REGISTER_COLLAB);
            }
        });

        this.db = FirebaseDatabase.getInstance().getReference();

        this.adapter = new FirebaseAdapter<UserModel>(this.db.child("users").orderByChild("boards/"+this.boardID).equalTo(true), UserModel.class, R.layout.list_collaborators, this){
            @Override
            protected void populateView(View v, UserModel model) {
                ((TextView)v.findViewById(R.id.collabName)).setText(model.getName());
            }
        };

        this.listView.setAdapter(this.adapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = adapter.getKey(i);
                Intent intent = new Intent(CollaboratorsActivity.this, CollaboratorDetails.class);
                intent.putExtra("user_key", key);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, DETAILS_COLLAB);
            }
        });
    }
}
