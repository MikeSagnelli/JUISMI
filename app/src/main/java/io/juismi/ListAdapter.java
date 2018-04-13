package io.juismi;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by zubut on 4/12/18.
 */

public class ListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Integer> colors;
    private int[] res;
    private int tagColor;

    public ListAdapter(Activity activity, ArrayList<Integer> colors, int[] res){
        this.activity = activity;
        this.colors = colors;
        this.res = res;
    }


    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Integer getItem(int i) {
        return this.colors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getTagColor(){
        return this.tagColor;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {

            // crear el view por primera vez
            view = activity.getLayoutInflater().inflate(
                    R.layout.tag_row,
                    null
            );
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.newTagName);
            checkBox.setBackgroundColor(this.res[i]);
            checkBox.setText(Integer.toString(i));
            checkBox.setTextColor(this.res[i]);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        int index = Integer.parseInt(checkBox.getText().toString());
                        tagColor = getItem(index);
                    }
                }
            });
        }

        return view;
    }
}
