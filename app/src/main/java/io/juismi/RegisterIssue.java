package io.juismi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterIssue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_issue);
    }

    public void saveButtonClicked(View v){
        Log.wtf("d", "save clicked");
        String puntos = ((EditText)findViewById(R.id.points_input)).getText().toString();
        IssueModel newIssue = new IssueModel(
                ((EditText)findViewById(R.id.name_input)).getText().toString(),
                ((EditText)findViewById(R.id.description_input)).getText().toString(),
                ((EditText)findViewById(R.id.tag_input)).getText().toString(),
                ((EditText)findViewById(R.id.status_input)).getText().toString(),
                ((EditText)findViewById(R.id.comment_input)).getText().toString(),
                ((EditText)findViewById(R.id.assignedTo_input)).getText().toString(),
                5
        );

        /*String messageText = ((EditText)findViewById(R.id.message)).getText().toString();
        if(messageText.equals("")){

        }else{
            Intent intent = new Intent();
            intent.putExtra(Intent_Constants.INTENT_MESSAGE_FIELD, messageText);
            setResult(Intent_Constants.INTENT_RESULT_CODE, intent);
            finish();
        }*/
    }
}
