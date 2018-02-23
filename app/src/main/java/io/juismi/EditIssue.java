package io.juismi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                ((EditText)findViewById(R.id.editName)).getText().toString(),
                ((EditText)findViewById(R.id.editDescription)).getText().toString(),
                ((EditText)findViewById(R.id.editTag)).getText().toString(),
                ((EditText)findViewById(R.id.editStatus)).getText().toString(),
                ((EditText)findViewById(R.id.editComment)).getText().toString(),
                ((EditText)findViewById(R.id.editAssigned)).getText().toString(),
                5
        );

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditIssue.this, IssueDetail.class));
            }
        });
    }
}
