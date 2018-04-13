package io.juismi;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by zubut on 4/13/18.
 */

public class UserFilterDialog extends Dialog {

    private Activity activity;
    private DatabaseReference mDatabase;
    private String boardID;
    private ListView listView;
    private FirebaseAdapter adapter;
    private String selectedUser;
    private ArrayList<String> usersIDs;
    private CheckBox checkBox;
    private ArrayList<CheckBox> checkBoxes;

    public UserFilterDialog(Activity activity, String boardID) {
        super(activity);
        this.activity = activity;
        this.boardID = boardID;
        this.usersIDs = new ArrayList<>();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_tag);

        this.listView = (ListView) findViewById(R.id.listView);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setListView();

    }

    private void setListView() {
        this.checkBoxes = new ArrayList<>();
        this.adapter = new FirebaseAdapter<TagModel>(this.mDatabase.child("users").orderByChild("boards/" + this.boardID).equalTo(true), TagModel.class, R.layout.tag_row, this.activity) {
            @Override
            protected void populateView(View v, TagModel model) {
                int index = getModels().indexOf(model);
                String key = getKey(index);
                usersIDs.add(key);
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

    public String getSelectedUser() {
        return this.selectedUser;
    }

}