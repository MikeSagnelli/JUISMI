package io.juismi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

public class TagFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ListView tagsList;
    private FirebaseAdapter<TagModel> adapter;
    private CheckBox checkBox;
    private String boardID;

    public TagFragment() {}

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
        View v = inflater.inflate(R.layout.fragment_tag, container, false);
        this.tagsList = (ListView) v.findViewById(R.id.listTags);

        //HARDCODED
        this.adapter = new FirebaseAdapter<TagModel>(this.mDatabase.child("tags").orderByChild("boardID").equalTo(this.boardID), TagModel.class,R.layout.tag_row, getActivity()) {
            @Override
            protected void populateView(View v, TagModel model) {
                checkBox = v.findViewById(R.id.name);
                checkBox.setText(model.getName());
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                //ImageView imageView = (ImageView) v.findViewById(R.id.color);
                checkBox.setBackgroundColor(Color.parseColor(hexColor));
            }
        };

        this.tagsList.setAdapter(this.adapter);
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
