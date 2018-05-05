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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentDialogFragment extends Dialog {

    private Activity activity;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String issueID;
    private Button submit, cancel;
    private EditText comment;

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

        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
                dismiss();
            }
        });

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void submitComment(){
        DatabaseReference ref = mDatabase.child("comments").push();
        ref.child("comment").setValue(this.comment.getText().toString());
        ref.child("issueID").setValue(this.issueID);
        String commentID = ref.getKey();
        this.mDatabase.child("issues").child(this.issueID).child("comments").child(commentID).setValue(true);

    }

}
