package io.juismi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


/**
 * Created by JARVIS on 11/04/2018.
 */

public class AllFragment extends Fragment{

    private ListView listView, tagsList;
    private FloatingActionButton addIssue;
    private Spinner filter;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private FirebaseAdapter adapter, tagsAdapter, tagRowAdapter;
    private String boardID;
    private TagFilterDialog tfd;
    private UserFilterDialog ufd;
    private AdapterView.OnItemClickListener listener, listener2;

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
        this.tagsList = view.findViewById(R.id.tagsList);
        this.addIssue = getActivity().findViewById(R.id.addIssue);
        this.filter = (Spinner) view.findViewById(R.id.filter);
        this.listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = adapter.getKey(i);
                Intent intent = new Intent(getActivity(), IssueDetail.class);
                intent.putExtra("issue_key", key);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, ISSUE_DETAILS);
            }
        };

        this.listener2 = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = tagsAdapter.getKey(i);
                Intent intent = new Intent(getActivity(), IssueDetail.class);
                intent.putExtra("issue_key", key);
                intent.putExtra("board_key", boardID);
                startActivityForResult(intent, ISSUE_DETAILS);
            }
        };

        String[] filters = new String[]{"Clear Filter","Filter by User","Filter by Tag"};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filters);
        this.filter.setAdapter(filterAdapter);
        this.filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filter.setSelection(-1);
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
                } else if(i == 1){
                    ufd = new UserFilterDialog(getActivity(), boardID);
                    ufd.show();
                    ufd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            String selectedUser = ufd.getSelectedUser();
                            if(selectedUser != null){
                                filterByUser(selectedUser);
                            }
                        }
                    });
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

        return view;
    }

    private void setAdapter(String key, String value){
        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild(key).equalTo(value), IssueModel.class,R.layout.list_issue, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatus());
                ((TextView)v.findViewById(R.id.storyPoints)).setText("Priority: " + String.valueOf(model.getPoints()));

                int index = getModels().indexOf(model);
                String key = getKey(index);
                ListView tagsList = (ListView) v.findViewById(R.id.tagsListView);
            }
        };
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this.listener);

        this.tagsAdapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild(key).equalTo(value), IssueModel.class,R.layout.issues_tag, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                int index = getModels().indexOf(model);
                String key = getKey(index);
                ListView tagsList = (ListView) v.findViewById(R.id.tagsListView);
                fillTags(key, tagsList);
            }
        };
        this.tagsList.setAdapter(this.tagsAdapter);
        this.tagsList.setOnItemClickListener(this.listener2);
    }

    private void filterByTag(String tagID){
        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild("tags/"+tagID).equalTo(true), IssueModel.class,R.layout.list_issue, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatusId());
                ((TextView)v.findViewById(R.id.storyPoints)).setText("Priority: " + String.valueOf(model.getPoints()));
            }
        };
        this.listView.setAdapter(this.adapter);

        this.tagsAdapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild("tags/"+tagID).equalTo(true), IssueModel.class,R.layout.issues_tag, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                int index = getModels().indexOf(model);
                String key = getKey(index);
                ListView tagsList = (ListView) v.findViewById(R.id.tagsListView);
                fillTags(key, tagsList);
            }
        };
        this.tagsList.setAdapter(this.tagsAdapter);
    }

    private void filterByUser(String userID){
        this.adapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild("board_user").equalTo(boardID+"_"+userID), IssueModel.class,R.layout.list_issue, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatusId());
                ((TextView)v.findViewById(R.id.storyPoints)).setText("Story Points: " + String.valueOf(model.getPoints()));
            }
        };
        this.listView.setAdapter(this.adapter);

        this.tagsAdapter = new FirebaseAdapter<IssueModel>(this.db.child("issues").orderByChild("board_user").equalTo(boardID+"_"+userID), IssueModel.class,R.layout.issues_tag, getActivity()){
            @Override
            protected void populateView(View v, IssueModel model) {
                int index = getModels().indexOf(model);
                String key = getKey(index);
                ListView tagsList = (ListView) v.findViewById(R.id.tagsListView);
                fillTags(key, tagsList);
            }
        };
        this.tagsList.setAdapter(this.tagsAdapter);
    }

    private void fillTags(String issueID, ListView listView){
        this.tagRowAdapter = new FirebaseAdapter<TagModel>(this.db.child("tags").orderByChild("issues/"+issueID).equalTo(true), TagModel.class,R.layout.detail_tag_row, getActivity()){

            @Override
            protected void populateView(View v, TagModel model) {
                RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.rowLayout);
                int index = getModels().indexOf(model);
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                layout.setBackgroundColor(Color.parseColor(hexColor));
            }
        };
        listView.setAdapter(this.tagRowAdapter);
    }
}
