package io.juismi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class IssuesView extends AppCompatActivity {
    private ListView listView;
    public static ArrayList<IssueModel> issueModels;
    private static CustomViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues_view);
        listView = (ListView) findViewById(R.id.listview);

        issueModels = new ArrayList<>();
        issueModels.add(new IssueModel("Listview con Issues","4", 2));
        issueModels.add(new IssueModel("Adapter para Listview","5", 0));
        issueModels.add(new IssueModel("Definir vistas","6", 1));
        issueModels.add(new IssueModel("Mockups","7", 1));

        adapter = new CustomViewAdapter(issueModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.wtf("ayuda", "ayuda");
            }
        });
    }

    public static void registerIssue(IssueModel issue){
        issueModels.add(issue);
    }
}
