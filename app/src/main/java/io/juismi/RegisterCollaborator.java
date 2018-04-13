package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterCollaborator extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String userID;

    private TextView email;

    private String boardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_collaborator);

        this.boardID = getIntent().getStringExtra("board_key");

        this.email = findViewById(R.id.email);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveButtonClicked(View v){

        this.mDatabase.child("users").orderByChild("email").equalTo(this.email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    userID = snap.getKey();
                }
                mDatabase.child("users").child(userID).child("boards").child(boardID).setValue(true);
                mDatabase.child("boards").child(boardID).child("team").child(userID).setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
