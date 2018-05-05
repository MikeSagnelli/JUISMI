package io.juismi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IssuesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private String boardID;
    private String userName;
    private TextView name;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;

    private static final int REGISTER_ISSUE = 0;
    private static final int ISSUE_DETAILS = 1;
    private static final int CALENDAR = 2;
    private static final int COLLABORATORS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
        this.db = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.db.child("users").child(this.user.getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
                if(name != null){
                    name.setText(userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getMenuInflater().inflate(R.menu.home, menu);
        this.name = (TextView) findViewById(R.id.userName);
        if(this.userName != null){
            this.name.setText(this.userName);
        }
        TextView email = (TextView) findViewById(R.id.eMail);
        email.setText(this.user.getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_button) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "JUISMI");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=io.juismi \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
        } else if (id == R.id.nav_calendar) {
            Intent c = new Intent(IssuesActivity.this, CalendarActivity.class);
            c.putExtra("board_key", this.boardID);
            startActivityForResult(c, CALENDAR);
        } else if (id == R.id.nav_add_users){
            Intent c = new Intent(IssuesActivity.this, CollaboratorsActivity.class);
            c.putExtra("board_key", this.boardID);
            startActivityForResult(c, COLLABORATORS);
        } else if (id == R.id.nav_share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "JUISMI");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=io.juismi \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
