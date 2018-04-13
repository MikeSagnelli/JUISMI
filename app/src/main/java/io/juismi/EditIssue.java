package io.juismi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditIssue extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private Spinner status;
    private String key,
                   boardID, userID;
    private TextView name, description, points;
    private FirebaseAdapter adapter;
    private ArrayList<String> tags, allTags;
    private ArrayList<CheckBox> checkBoxes;
    private ListView tagsList;
    private AssignDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_edit);

        this.status = (Spinner) findViewById(R.id.status_input2);
        this.name = (TextView) findViewById(R.id.name_input2);
        this.description = (TextView) findViewById(R.id.description_input2);
        this.points = (TextView) findViewById(R.id.points_input2);
        this.tagsList = (ListView) findViewById(R.id.listView);

        Intent intent = getIntent();
        this.key = intent.getStringExtra("issue_key");
        this.boardID = intent.getStringExtra("board_key");
        this.tags = intent.getStringArrayListExtra("tags");

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance().getReference();
        this.dialog =  new AssignDialog(this.boardID, this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.status.setAdapter(adapter);

        Map<String, Object> map = new HashMap<String,Object>();
        Query query = db.child("issues").child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.wtf("name", dataSnapshot.getValue().toString());
                    HashMap map = (HashMap)dataSnapshot.getValue();
                    name.setText(map.get("name").toString());
                    description.setText(map.get("description").toString());
                    if(map.get("userID") != null){
                        userID = map.get("userID").toString();
                    }
                    switch(map.get("status").toString()){
                        case "To Do":
                            status.setSelection(0);
                            break;
                        case "In Progress":
                            status.setSelection(1);
                            break;
                        case "Done":
                            status.setSelection(2);
                            break;
                    }
                    points.setText(map.get("points").toString());
                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setListView();
    }

    public void saveButtonClicked(View v){

        String status = ((Spinner) findViewById(R.id.status_input2)).getSelectedItem().toString();

        Map<String, Object> postValues = new HashMap<String,Object>();
        postValues.put("name", ((EditText) findViewById(R.id.name_input2)).getText().toString());
        postValues.put("description", ((EditText) findViewById(R.id.description_input2)).getText().toString());
        postValues.put("status", status);
        postValues.put("points", Integer.parseInt(((EditText) findViewById(R.id.points_input2)).getText().toString()));
        postValues.put("userID", this.userID);

        db.child("issues").child(this.key).updateChildren(postValues);
        db.child("issues").child(this.key).child("board_status").setValue(this.boardID+"_"+status);

        this.saveTags(this.key);

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void setListView(){
        this.checkBoxes = new ArrayList<>();
        this.allTags = new ArrayList<>();
        this.adapter = new FirebaseAdapter<TagModel>(this.mDatabase.child("tags").orderByChild("boardID").equalTo(this.boardID), TagModel.class,R.layout.tag_row, this) {
            @Override
            protected void populateView(View v, TagModel model) {
                int index = getModels().indexOf(model);
                String currentKey = getKey(index);
                CheckBox checkBox = v.findViewById(R.id.newTagName);
                allTags.add(currentKey);
                checkBoxes.add(checkBox);

                if(tags.contains(currentKey)){
                    checkBox.setChecked(true);
                }

                checkBox.setText(model.getName());
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                checkBox.setBackgroundColor(Color.parseColor(hexColor));
            }
        };

        this.tagsList.setAdapter(this.adapter);
    }

    private void saveTags(String issueID){
        for (int i = 0; i < this.allTags.size(); i++) {
            String tagID = this.allTags.get(i);
            boolean selected = this.checkBoxes.get(i).isChecked();
            db.child("issues").child(issueID).child("tags").child(tagID).setValue(selected);
            db.child("tags").child(tagID).child("issues").child(issueID).setValue(selected);
        }

    }

    public void assignUser(View v){
        this.dialog.show();
        this.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                userID = dialog.getSelectedUser();
            }
        });
    }
}
