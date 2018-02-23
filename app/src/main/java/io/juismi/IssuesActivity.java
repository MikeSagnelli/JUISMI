package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class IssuesActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton addIssue;
    private Spinner status;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private FirebaseAdapter adapter;

    private static final int REGISTER_ISSUE = 0;
    private static final int ISSUE_DETAILS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        this.status = (Spinner) findViewById(R.id.filter_status);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_status, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.status.setAdapter(spinnerAdapter);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

        this.db = FirebaseDatabase.getInstance().getReference();

        this.listView = (ListView) findViewById(R.id.issuesList);
        this.addIssue = (FloatingActionButton) findViewById(R.id.addIssue);

        this.addIssue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(IssuesActivity.this, RegisterIssue.class);
                startActivityForResult(intent, REGISTER_ISSUE);
            }
        });

        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child(this.user.getUid()).child("issues"), IssueModel.class,R.layout.list_issue, this){
            @Override
            protected void populateView(View v, IssueModel model) {
                ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatusId());
                ((TextView)v.findViewById(R.id.storyPoints)).setText("Story Points: " + String.valueOf(model.getPoints()));
            }
        };

        this.listView.setAdapter(this.adapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = adapter.getKey(i);
                Intent intent = new Intent(IssuesActivity.this, IssueDetail.class);
                intent.putExtra("issue_key", key);
                startActivityForResult(intent, ISSUE_DETAILS);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_ISSUE && resultCode == Activity.RESULT_OK){

            Toast.makeText(this, "Added issue", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ISSUE_DETAILS && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Edited successfully", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ISSUE_DETAILS && resultCode == 7){
            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
