package io.juismi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Manlio GR on 22/02/2018.
 */

public class IssueDetail extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_detail);

        TextView name = (TextView) findViewById(R.id.nameIssue);
        TextView description = (TextView) findViewById(R.id.descriptionIssue);
        TextView tag = (TextView) findViewById(R.id.tagIssue);
        TextView status = (TextView) findViewById(R.id.statusIssue);
        //TextView comment = (TextView) findViewById(R.id.commentIssue);
        TextView assignedTo = (TextView) findViewById(R.id.assignedIssue);
        TextView points = (TextView) findViewById(R.id.pointsIssue);

        Button boton = (Button) findViewById(R.id.editButton);

       // issueModels = new ArrayList<>();
       // issueModels.add(new IssueModel("Listview con Issues","4", 2));
       // public IssueModel(String name, String description, String tag, String status, String comment, String assignedTo, int points

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IssueDetail.this, EditIssue.class));
            }
        });
    }
}
