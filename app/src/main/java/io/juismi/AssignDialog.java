package io.juismi;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by zubut on 4/13/18.
 */

public class AssignDialog extends Dialog {

    private Activity activity;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button submit, cancel;
    private String boardID, selectedUser;
    private ListView listView;
    private FirebaseAdapter adapter;
    private ArrayList<CheckBox> checkBoxes;
    private ArrayList<String> usersIDs;
    private CheckBox checkBox;

    public AssignDialog(String boardID, Activity activity) {
        super(activity);
        this.activity = activity;
        this.boardID = boardID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.assign_dialog);
        this.listView = (ListView) findViewById(R.id.listView);
        this.submit = (Button) findViewById(R.id.submitUser);
        this.cancel = (Button) findViewById(R.id.cancelUser);
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setListView();
    }

    private void setListView(){
        this.checkBoxes = new ArrayList<>();
        this.usersIDs = new ArrayList<>();
        this.adapter = new FirebaseAdapter<UserModel>(this.mDatabase.child("users").orderByChild("boards/"+this.boardID).equalTo(true), UserModel.class,R.layout.tag_row, this.activity) {

            @Override
            protected void populateView(View v, UserModel model) {
                int index = getModels().indexOf(model);
                String key = getKey(index);
                usersIDs.add(key);
                checkBox = v.findViewById(R.id.newTagName);
                checkBox.setText(model.getName());
                checkBoxes.add(checkBox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        int index = checkBoxes.indexOf(compoundButton);
                        selectedUser = usersIDs.get(index);
                        dismiss();
                    }
                });
            }
        };

        this.listView.setAdapter(this.adapter);
    }

    public String getSelectedUser(){
        return this.selectedUser;
    }
}
