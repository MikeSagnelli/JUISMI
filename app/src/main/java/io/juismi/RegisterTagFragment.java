package io.juismi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterTagFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button submit, cancel;
    private EditText tagName;
    private String boardID;

    private OnFragmentInteractionListener mListener;

    public RegisterTagFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            boardID = getArguments().getString("boardID");
        }
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_tag, container, false);

        this.tagName = (EditText) v.findViewById(R.id.tag_input);
        this.cancel = (Button) v.findViewById(R.id.cancel);
        this.submit = (Button) v.findViewById(R.id.saveTagButton);
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            submitTag();
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction();
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void submitTag(){
        //HARDCODED
        int color = Color.parseColor("#FF33B5E5");

        String name = this.tagName.getText().toString();
        TagModel newTag = new TagModel(color, name, BoardsActivity.boardID);

        //HARDCODED
        DatabaseReference tagref = mDatabase.child("tags").push();
        DatabaseReference boardref = mDatabase.child("boards").child(this.boardID).child("tags").push();
        tagref.setValue(newTag);
        boardref.setValue(newTag);
        String tagID = tagref.getKey();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

}
