package io.juismi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by JARVIS on 11/04/2018.
 */

public class AllFragment extends Fragment{

    private ListView listView;
    private FloatingActionButton addIssue;
    private Spinner filter;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private FirebaseAdapter adapter;
    private String boardID;
    private TagFilterDialog tfd;

    private static final int REGISTER_ISSUE = 0;
    private static final int ISSUE_DETAILS = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_fragment,container,false);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.boardID = getArguments().getString("board_key");

        this.db = FirebaseDatabase.getInstance().getReference();

        this.listView = view.findViewById(R.id.issuesList);
        this.addIssue = getActivity().findViewById(R.id.addIssue);
        this.filter = (Spinner) view.findViewById(R.id.filter);

        String[] filters = new String[]{"Clear Filter","Filter by User","Filter by Tag",""};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filters);
        this.filter.setAdapter(filterAdapter);
        this.filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filter.setSelection(3);
                if(i == 2){
                    tfd = new TagFilterDialog(getActivity(), boardID);
                    tfd.show();
                    tfd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            String selectedTag = tfd.getSelectedTag();
                            if(selectedTag != null){
                                filterByTag(selectedTag);
                            }
                        }
                    });
                } else if(i == 0){
                    setAdapter("boardID", boardID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.addIssue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), RegisterIssue.class);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, REGISTER_ISSUE);
            }
        });
        this.db.child("tags").orderByChild("boardID").equalTo(this.boardID);
        this.setAdapter("boardID", this.boardID);


        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = adapter.getKey(i);
                Intent intent = new Intent(getActivity(), IssueDetail.class);
                intent.putExtra("issue_key", key);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, ISSUE_DETAILS);
            }
        });

        return view;
    }

    private void setAdapter(String key, String value){
        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild(key).equalTo(value), IssueModel.class,R.layout.list_issue, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatusId());
                ((TextView)v.findViewById(R.id.storyPoints)).setText("Story Points: " + String.valueOf(model.getPoints()));
            }
        };
        this.listView.setAdapter(this.adapter);
    }

    private void filterByTag(String tagID){
        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild("tags/"+tagID).equalTo(true), IssueModel.class,R.layout.list_issue, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatusId());
                ((TextView)v.findViewById(R.id.storyPoints)).setText("Story Points: " + String.valueOf(model.getPoints()));
            }
        };
        this.listView.setAdapter(this.adapter);
    }
}
