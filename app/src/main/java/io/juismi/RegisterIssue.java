package io.juismi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterIssue extends AppCompatActivity implements TagFragment.OnFragmentInteractionListener, RegisterTagFragment.OnFragmentInteractionListener{

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAdapter mAdapter;
    private  TagFragment tagFragment;
    private RegisterTagFragment registerTagFragment;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_issue);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.bundle = new Bundle();
        this.bundle.putString("boardID", BoardsActivity.boardID);
        this.tagFragment = new TagFragment();
        this.tagFragment.setArguments(this.bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, this.tagFragment, "tagsList");

        ft.commit();

   }

    public void saveButtonClicked(View v){

        IssueModel newIssue = new IssueModel(
                ((EditText) findViewById(R.id.name_input)).getText().toString(),
                ((EditText) findViewById(R.id.description_input)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.points_input)).getText().toString()),
                "To Do",
                new ArrayList<Integer>()
        );
        newIssue.addInt(5);

        mDatabase.child(user.getUid()).child("issues").push().setValue(newIssue);

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public void addTag(View v){
        FragmentManager fm = getFragmentManager();
        Fragment f = fm.findFragmentByTag("tagsList");

        if(f != null){
            FragmentTransaction ft = fm.beginTransaction();
            this.registerTagFragment = new RegisterTagFragment();
            this.registerTagFragment.setArguments(this.bundle);
            ft.remove(f);
            ft.add(R.id.container, this.registerTagFragment, "registerTag");
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction() {

    }
}
