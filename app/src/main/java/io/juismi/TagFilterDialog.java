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

public class TagFilterDialog extends Dialog {

    private Activity activity;
    private DatabaseReference mDatabase;
    private String boardID;
    private ListView listView;
    private FirebaseAdapter adapter;
    private String selectedTag;
    private ArrayList<String> tagsIDs;
    private CheckBox checkBox;
    private ArrayList<CheckBox> checkBoxes;

    public TagFilterDialog(Activity activity, String boardID){
        super(activity);
        this.activity = activity;
        this.boardID = boardID;
        this.tagsIDs = new ArrayList<>();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_tag);

        int[] res = this.activity.getResources().getIntArray(R.array.androidcolors);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int i : res)
        {
            colors.add(i);
        }

        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setAdapter(adapter);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.setListView();

    }

    private void setListView(){
        this.checkBoxes = new ArrayList<>();
        this.adapter = new FirebaseAdapter<TagModel>(this.mDatabase.child("tags").orderByChild("boardID").equalTo(this.boardID), TagModel.class,R.layout.tag_row, this.activity) {
            @Override
            protected void populateView(View v, TagModel model) {
                int index = getModels().indexOf(model);
                String key = getKey(index);
                tagsIDs.add(key);
                checkBox = v.findViewById(R.id.newTagName);
                checkBoxes.add(checkBox);
                checkBox.setText(model.getName());
                String hexColor = String.format("#%06X", (0xFFFFFF & model.getColor()));
                checkBox.setBackgroundColor(Color.parseColor(hexColor));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        int index = checkBoxes.indexOf(compoundButton);
                        Log.d("checked", checkBox.getText().toString());
                        Log.d("list", checkBoxes.get(0).getText().toString());
                        selectedTag = tagsIDs.get(index);
                        dismiss();
                    }
                });
            }
        };

        this.listView.setAdapter(this.adapter);
    }

    public String getSelectedTag(){
        return this.selectedTag;
    }

}
