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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TagFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ListView tagsList;
    private FirebaseAdapter<TagModel> adapter;
    private CheckBox checkBox;

    public TagFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TagFragment newInstance(String param1, String param2) {
        TagFragment fragment = new TagFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        this.adapter = new FirebaseAdapter<TagModel>(this.mDatabase.child("boards").child("-L9sfw0CIdrHfXEEtdHQ").child("tags"), TagModel.class,R.layout.tag_row, getActivity()) {
            @Override
            protected void populateView(View v, TagModel model) {
                checkBox = v.findViewById(R.id.name);
                checkBox.setText(model.getName());
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                //ImageView imageView = (ImageView) v.findViewById(R.id.color);
                checkBox.setBackgroundColor(Color.parseColor(hexColor));
                Log.d("color", hexColor);
            }
        };

        this.tagsList.setAdapter(this.adapter);
        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
