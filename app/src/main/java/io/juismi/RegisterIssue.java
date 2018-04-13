package io.juismi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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

public class RegisterIssue extends AppCompatActivity{

    private DatabaseReference mDatabase;
    private String boardID, userID;
    private List<String> tags;
    private ListView tagsList;
    private FirebaseAdapter<TagModel> adapter;
    private ArrayList<CheckBox> checkBoxes;
    private AssignDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_issue);

        Intent intent = getIntent();
        this.boardID = intent.getStringExtra("board_key");
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.tagsList = (ListView) findViewById(R.id.tagslistView);
        this.setListView();

        this.dialog = new AssignDialog(this.boardID, this);
   }

    public void saveButtonClicked(View v){


        IssueModel newIssue = new IssueModel(
                ((EditText) findViewById(R.id.name_input)).getText().toString(),
                ((EditText) findViewById(R.id.description_input)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.points_input)).getText().toString()),
                "To Do",
                this.boardID, this.userID
        );

        DatabaseReference ref = mDatabase.child("issues").push();
        String issueID = ref.getKey();
        ref.setValue(newIssue);
        ref.child("board_status").setValue(this.boardID+"_To Do");

        mDatabase.child("boards").child(this.boardID).child("issues").child(issueID).setValue(true);
        this.saveTags(issueID);

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public void addTag(View v){
        TagDialogFragment tdf = new TagDialogFragment(this.boardID, this);
        tdf.show();
        tdf.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setListView();
            }
        });
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

    private void saveTags(String issueID){
        for (int i = 0; i < this.tags.size(); i++) {
            String tagID = this.tags.get(i);
            boolean selected = this.checkBoxes.get(i).isChecked();
            if(selected){
                this.mDatabase.child("issues").child(issueID).child("tags").child(tagID).setValue(selected);
                this.mDatabase.child("tags").child(tagID).child("issues").child(issueID).setValue(selected);
            }
        }

    }

    private void setListView(){
        this.checkBoxes = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.adapter = new FirebaseAdapter<TagModel>(this.mDatabase.child("tags").orderByChild("boardID").equalTo(this.boardID), TagModel.class,R.layout.tag_row, this) {
            @Override
            protected void populateView(View v, TagModel model) {
                int index = getModels().indexOf(model);
                String currentKey = getKey(index);
                CheckBox checkBox = v.findViewById(R.id.newTagName);

                tags.add(currentKey);
                checkBoxes.add(checkBox);
                checkBox.setText(model.getName());
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                checkBox.setBackgroundColor(Color.parseColor(hexColor));
            }
        };

        this.tagsList.setAdapter(this.adapter);
    }

}
