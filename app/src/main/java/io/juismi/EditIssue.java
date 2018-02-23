package io.juismi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Manlio GR on 22/02/2018.
 */

public class EditIssue extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_edit);

        Button boton2 = (Button) findViewById(R.id.saveButton);

        IssueModel newIssue = new IssueModel(
                ((EditText) findViewById(R.id.name_input)).getText().toString(),
                ((EditText) findViewById(R.id.description_input)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.points_input)).getText().toString()),
                ((Spinner) findViewById(R.id.status_input)).getSelectedItem().toString(),
                new ArrayList<Integer>()
        );

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditIssue.this, IssueDetail.class));
            }
        });
    }
}
