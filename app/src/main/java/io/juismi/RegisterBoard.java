package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterBoard extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_board);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveButtonClicked(View v){

        BoardModel newBoard = new BoardModel(
                ((EditText) findViewById(R.id.name_input)).getText().toString(),
                this.user.getUid()
        );

        DatabaseReference boardRef = mDatabase.child("boards").push();
        String boardID = boardRef.getKey();
        boardRef.setValue(newBoard);
        boardRef.child("team").child(this.user.getUid()).setValue(true);

        mDatabase.child("users").child(this.user.getUid()).child("boards").child(boardID).setValue(true);

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
