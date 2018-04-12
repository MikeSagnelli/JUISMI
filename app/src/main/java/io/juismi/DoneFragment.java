package io.juismi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JARVIS on 11/04/2018.
 */

public class DoneFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton addIssue;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private FirebaseAdapter adapter;

    private static final int REGISTER_ISSUE = 0;
    private static final int ISSUE_DETAILS = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.done_fragment,container,false);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

        this.db = FirebaseDatabase.getInstance().getReference();

        this.listView = view.findViewById(R.id.issuesList);
        this.addIssue = getActivity().findViewById(R.id.addIssue);

        this.addIssue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), RegisterIssue.class);
                startActivityForResult(intent, REGISTER_ISSUE);
            }
        });

        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child(this.user.getUid()).child("issues").orderByChild("statusId").equalTo("Done"), IssueModel.class,R.layout.list_issue, getActivity()){
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
                Intent intent = new Intent(getActivity(), IssueDetail.class);
                intent.putExtra("issue_key", key);
                startActivityForResult(intent, ISSUE_DETAILS);
            }
        });

        return view;
    }
}