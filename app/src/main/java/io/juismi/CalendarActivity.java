package io.juismi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private CompactCalendarView compactCalendar;
    private String boardId;
    private TextView textView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private FirebaseAuth mAuth;
    private List<IssueModel> issues;
    private DatabaseReference db;
    private SimpleDateFormat sdf;
    private ListView lv;
    private FirebaseAdapter<IssueModel> adapter;
    private AdapterView.OnItemClickListener listener;

    private static final int ISSUE_DETAILS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        this.boardId = getIntent().getStringExtra("board_key");
        this.sdf = new SimpleDateFormat("dd MM yyyy");
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        this.issues = new ArrayList();
        this.lv = (ListView) findViewById(R.id.issues_list);
        this.listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = adapter.getKey(i);
                Intent intent = new Intent(CalendarActivity.this, IssueDetail.class);
                intent.putExtra("issue_key", key);
                intent.putExtra("board_key", boardId);
                startActivityForResult(intent, ISSUE_DETAILS);
            }
        };

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        actionBar.setTitle(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        textView = (TextView) findViewById(R.id.textview_calendar);

        final List<Event> issuesEvents = new ArrayList<>();
        long timeInMilliseconds = 0;

        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseDatabase.getInstance().getReference();
        Query query = this.db.child("issues").orderByChild("boardId").equalTo(this.boardId);
        ChildEventListener childEventListener;
        childEventListener = query.getRef().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.wtf("hola", "Entered added");
                issues.clear();
                IssueModel issue = dataSnapshot.getValue(IssueModel.class);
                issues.add(issue);
                Log.wtf("hey tu", issue.getName());
                long timeInMilliseconds;
                try {
                    //pass the date of IssueModel to the function below
                    Date mDate = sdf.parse(issue.getDueDate());
                    Log.wtf("fecha", mDate.toString());
                    //timeInMilliseconds is the value of time used to insert the issue onto the calendar
                    timeInMilliseconds = mDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                    timeInMilliseconds = 0;
                }
                Event ev = new Event (Color.BLUE, timeInMilliseconds, issue.getName().toString());
                issuesEvents.add(ev);
                compactCalendar.addEvents(issuesEvents);
                compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                    @Override
                    public void onDayClick(Date dateClicked) {
                        Context context = getApplicationContext();
                        if(compactCalendar.getEvents(dateClicked).size() == 0){
                            //Toast.makeText(context, "No hay tareas para este día.", Toast.LENGTH_SHORT).show();
                            textView.setText("No issues for today.");
                        }else{
                            List<Event> issuesOnDate = compactCalendar.getEvents(dateClicked);
                            String issuesListString="Issues: \n";
                            adapter = new FirebaseAdapter<IssueModel>(db.child("issues").orderByChild("board_date").equalTo(boardId+"_"+dateClicked.toString()), IssueModel.class, R.layout.list_issue, CalendarActivity.this) {
                                @Override
                                protected void populateView(View v, IssueModel model) {
                                    ((TextView)v.findViewById(R.id.nameTask)).setText(model.getName());
                                    ((TextView)v.findViewById(R.id.issueStatus)).setText("Status: " + model.getStatus());
                                    ((TextView)v.findViewById(R.id.storyPoints)).setText("Priority: " + String.valueOf(model.getPoints()));

                                    int index = getModels().indexOf(model);
                                    String key = getKey(index);
                                    ListView tagsList = (ListView) v.findViewById(R.id.tagsListView);
                                }
                            };
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(listener);
                            //Toast.makeText(context, tasksString,Toast.LENGTH_SHORT).show();
                            textView.setText(issuesListString);
                        }
                    }

                    @Override
                    public void onMonthScroll(Date firstDayOfNewMonth) {
                        actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }


        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ISSUE_DETAILS && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Edited issue successfully", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ISSUE_DETAILS && resultCode == 7){
            Toast.makeText(this, "Deleted issue successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
