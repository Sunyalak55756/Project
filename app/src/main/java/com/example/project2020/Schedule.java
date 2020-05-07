package com.example.project2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project2020.utility.AlarmReceiver;

import java.util.Calendar;

import java.util.TimeZone;

public class Schedule extends AppCompatActivity {
    Button date;
    Button time;
    Button enter;
    EditText description;
    ImageView gogo;
    EditText data;
    TextView texttime;
    TimePickerDialog mTimePicker;
    Calendar c = Calendar.getInstance();
    TextView textdate;
    int Hour;
    int min;
    DatePickerDialog datePickerDialog;
    int mYear = c.get(Calendar.YEAR); // current year
    int mMonth = c.get(Calendar.MONTH); // current month
    int mDay = c.get(Calendar.DAY_OF_MONTH); // current

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule);

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        textdate = findViewById(R.id.datetext);
        texttime = findViewById(R.id.timetext);
        gogo = findViewById(R.id.gogo);
        data = findViewById(R.id.data);
        enter = findViewById(R.id.enter);
        description = findViewById(R.id.description);



        gogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                startActivity(intent);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                // date picker dialog
                datePickerDialog = new DatePickerDialog(Schedule.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                textdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }

        });


        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(Schedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        texttime.setText(selectedHour + ":" + selectedMinute);
                        Hour = selectedHour;
                        min = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }


        });
        enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), ""+Hour, Toast.LENGTH_SHORT).show();


                if (data.getText().length() == 0||description.getText().length()==0||texttime.getText().length()==0||textdate.getText().length()==0) {
                    Toast.makeText(getApplicationContext(), "กรอกข้อมูลไม่ครบ", Toast.LENGTH_SHORT).show();

                } else {
               /*     long startTime = getLongAsDate(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth() + 1, datePickerDialog.getDatePicker().getDayOfMonth(), Hour, min);
                 long endTime = getLongAsDate(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth() + 1, datePickerDialog.getDatePicker().getDayOfMonth(), Hour + 1, min);
                    AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
                    // cal.add(Calendar.SECOND, 5);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);*/
                    addReminderInCalendar();
                    Toast.makeText(getApplicationContext(), "บันทึกการแจ้งเตือนแล้ว", Toast.LENGTH_SHORT).show();
                    texttime.setText("");
                    textdate.setText("");
                    data.setText(null);
                    description.setText(null);

                }


            }
        });

    }


    private void addReminderInCalendar() {

        Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");
        ContentResolver cr = getContentResolver();
        TimeZone timeZone = TimeZone.getDefault();


        long startTime = getLongAsDate(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth() + 1, datePickerDialog.getDatePicker().getDayOfMonth(), Hour, min);
        long endTime = getLongAsDate(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth() + 1, datePickerDialog.getDatePicker().getDayOfMonth(), Hour + 1, min);

        /** Inserting an event in calendar. */
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, "" + data.getText().toString());
        values.put(CalendarContract.Events.DESCRIPTION, ""+ description.getText().toString());
        values.put(CalendarContract.Events.ALL_DAY, 0);
        // event starts at 11 minutes from now
        values.put(CalendarContract.Events.DTSTART, startTime);
        // ends 60 minutes from now
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri event = cr.insert(EVENTS_URI, values);

        // Display event id
        //  Toast.makeText(getApplicationContext(), "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();

        /** Adding reminder for event added. */
        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
        values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES,10);

        cr.insert(REMINDERS_URI, values);
    }

    /**
     * Returns Calendar Base URI, supports both new and old OS.
     */
    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }

    public long getLongAsDate(int year, int month, int date, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        return cal.getTimeInMillis();
    }

}
