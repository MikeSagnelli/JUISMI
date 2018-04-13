package io.juismi;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;


public class TagDialogFragment extends Dialog{

    private Activity activity;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button submit, cancel;
    private EditText tagName;
    private String boardID;
    private ListView listView;
    private ListAdapter adapter;

    public TagDialogFragment(String boardID, Activity activity) {
        super(activity);
        this.activity = activity;
        this.boardID = boardID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_tag_dialog);
        this.tagName = (EditText) findViewById(R.id.newTagName);
        this.submit = (Button) findViewById(R.id.submitTag);
        this.cancel = (Button) findViewById(R.id.cancelTag);
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTag();
                dismiss();
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        int[] res = this.activity.getResources().getIntArray(R.array.androidcolors);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i : res)
        {
            colors.add(i);
        }

        this.listView = (ListView) findViewById(R.id.listView);
        this.adapter = new ListAdapter(this.activity, colors, res);
        this.listView.setAdapter(adapter);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void submitTag(){
        String name = this.tagName.getText().toString();
        TagModel newTag = new TagModel(this.adapter.getTagColor(), name, this.boardID);
        DatabaseReference tagref = mDatabase.child("tags").push();
        tagref.setValue(newTag);
        String tagID = tagref.getKey();
        this.mDatabase.child("boards").child(this.boardID).child("tags").child(tagID).setValue(true);

    }
}
