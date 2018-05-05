package io.juismi;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentDialogFragment extends Dialog {

    private Activity activity;
    private ArrayList<String> comments;
    private FirebaseAdapter commentsAdapter;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String issueID;
    private Button submit, cancel;
    private EditText comment;
    private ListView commentList;

    public CommentDialogFragment(String issueID, Activity activity) {
        // Required empty public constructor
        super(activity);
        this.activity = activity;
        this.issueID = issueID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_comment_dialog);


        this.comment = (EditText) findViewById(R.id.comment);
        this.submit = (Button) findViewById(R.id.submitComment);
        this.cancel = (Button) findViewById(R.id.cancelComment);
        this.commentList = (ListView) findViewById(R.id.commentsListView);

        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
                dismiss();
            }
        });

        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.setText("");
                dismiss();
            }
        });

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.comments = new ArrayList<>();
        this.commentsAdapter = new FirebaseAdapter<CommentModel>(this.mDatabase.child("comments").orderByChild("issueID").equalTo(issueID), CommentModel.class, R.layout.comment_row2, this.activity) {
            @Override
            protected void populateView(View v, final CommentModel model) {
                int index = getModels().indexOf(model);
                comments.add(getKey(index));
                TextView comment = v.findViewById(R.id.comment);
                comment.setText(model.getComment());
                Button delete = v.findViewById(R.id.deleteComment);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child("issues").child(issueID).child("comments").child(getKey(getModels().indexOf(model))).removeValue();
                        mDatabase.child("comments").child(getKey(getModels().indexOf(model))).removeValue();
                    }
                });
            }
        };

        this.commentList.setAdapter(this.commentsAdapter);
    }

    private void submitComment(){
        DatabaseReference ref = mDatabase.child("comments").push();
        ref.child("comment").setValue(this.comment.getText().toString());
        ref.child("issueID").setValue(this.issueID);
        String commentID = ref.getKey();
        this.mDatabase.child("issues").child(this.issueID).child("comments").child(commentID).setValue(true);

    }

}
