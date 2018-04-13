package io.juismi;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class IssuesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private String boardID;

    private static final int REGISTER_ISSUE = 0;
    private static final int ISSUE_DETAILS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        Intent intent = getIntent();
        this.boardID = intent.getStringExtra("board_key");

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager){
        Bundle b = new Bundle();
        b.putString("board_key", this.boardID);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        Fragment all = new AllFragment();
        Fragment toDo = new ToDoFragment();
        Fragment inProgress = new InProgressFragment();
        Fragment done = new DoneFragment();
        all.setArguments(b);
        toDo.setArguments(b);
        inProgress.setArguments(b);
        done.setArguments(b);
        adapter.addFragment(all, "All");
        adapter.addFragment(toDo, "To Do");
        adapter.addFragment(inProgress, "In Progress");
        adapter.addFragment(done, "Finished");
        viewPager.setAdapter(adapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_ISSUE && resultCode == Activity.RESULT_OK){

            Toast.makeText(this, "Added issue to To Do list", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ISSUE_DETAILS && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Edited issue successfully", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ISSUE_DETAILS && resultCode == 7){
            Toast.makeText(this, "Deleted issue successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
