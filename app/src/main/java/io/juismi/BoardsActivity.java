package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BoardsActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton addBoard;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    private FirebaseAdapter adapter;

    private static final int REGISTER_BOARD = 0;
    private static final int ISSUES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);
        getSupportActionBar().setTitle("BOARDS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.listView = (ListView)findViewById(R.id.listView);
        this.addBoard = (FloatingActionButton)findViewById(R.id.addBoard);

        this.addBoard.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(BoardsActivity.this, RegisterBoard.class);
                startActivityForResult(intent, REGISTER_BOARD);
            }
        });

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();

        this.db = FirebaseDatabase.getInstance().getReference();

        this.adapter = new FirebaseAdapter<BoardModel>(this.db.child("boards").orderByChild("team/"+this.user.getUid()).equalTo(true), BoardModel.class, R.layout.list_board, this) {
            @Override
            protected void populateView(View v, BoardModel model) {
                ((TextView)v.findViewById(R.id.eMail)).setText(model.getName());
            }
        };
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = adapter.getKey(position);
                Log.d("hey", key);
                Intent intent = new Intent(BoardsActivity.this, IssuesActivity.class);
                intent.putExtra("board_key", key);
                startActivityForResult(intent, ISSUES);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_BOARD && resultCode == Activity.RESULT_OK){

            Toast.makeText(this, "Created board", Toast.LENGTH_SHORT).show();
        }
    }
}
