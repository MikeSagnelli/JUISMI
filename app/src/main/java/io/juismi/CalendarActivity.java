package io.juismi;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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

    CompactCalendarView compactCalendar;
    TextView textView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM- yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        actionBar.setTitle(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        textView = (TextView) findViewById(R.id.textview_calendar);

        //Array where issues are stored as Event object.
        final List<Event> issues = new ArrayList<>();
        //Pass to this activity all the all the Issues in a List
            //for that iterates the List<IssueModel>
                //this code converts a Date in "dd MM yyyy" format to timeInMiliseconds.
                //Store the date in "dd mm yyyy" format in IssueModel
                    long timeInMilliseconds = 0;
                    try {
                        //pass the date of IssueModel to the function below
                        Date mDate = sdf.parse("02 05 2018");
                        //timeInMilliseconds is the value of time used to insert the issue onto the calendar
                        timeInMilliseconds = mDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //create event for every issue. With color, date and name of the issue.
                    Event ev1 = new Event (Color.RED, timeInMilliseconds, "Entrega de Memo");
                    Event ev2 = new Event (Color.RED, timeInMilliseconds, "Pagar la colegiatura");
                    //add the events to the issues list
                    issues.add(ev1);
                    issues.add(ev2);
        //add all issues to the calendar
        compactCalendar.addEvents(issues);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                if(compactCalendar.getEvents(dateClicked).size() == 0){
                    //Toast.makeText(context, "No hay tareas para este día.", Toast.LENGTH_SHORT).show();
                    textView.setText("No hay tareas para este día.");
                }else{
                    List<Event> issuesOnDate = compactCalendar.getEvents(dateClicked);
                    String issuesListString="Issues: \n";
                    for(int i=0;i<issuesOnDate.size();i++){
                        issuesListString = issuesListString + issuesOnDate.get(i).getData().toString() + "\n";
                    }
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
}
