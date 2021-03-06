package com.amko0l.ontime.ontime.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import com.amko0l.ontime.ontime.R;
import com.amko0l.ontime.ontime.database.DataValues;
import com.amko0l.ontime.ontime.database.OnTimeDB;
import com.amko0l.ontime.ontime.receivers.Alarm_Receiver;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lakshmisagar on 4/14/2017.
 */

public class AlarmActivity extends AppCompatActivity {
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    Calendar calendar;
    Context context;
    PendingIntent pending_intent;
    static SQLiteDatabase sqLiteDatabase;
    OnTimeDB onTimeDB;
    final ArrayList<Integer> list = new ArrayList<Integer>();
    AlarmManager[] alarmManager = new AlarmManager[24];
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    Intent alarm_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String frompd = intent.getStringExtra("notification");
        Log.d("Amit", "frompd " +frompd);
        if ("yes".equals(frompd)) {
            stopAlarm();
            Intent intent1 = new Intent(this, MapActivity.class);
            startActivity(intent1);
        }

        setContentView(R.layout.alarm_activity);
        this.context = this;
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        onTimeDB = new OnTimeDB(this, "OnTimeDB", null, 1);
        sqLiteDatabase = onTimeDB.getWritableDatabase();
        //initialise intent
         alarm_intent = new Intent(this.context, Alarm_Receiver.class);
        //initialise our timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        //create an instance of calendar
        final Calendar calendar = Calendar.getInstance();
        Button start_alarm = (Button) findViewById(R.id.start_alarm);

        //Create an onClick listener to start the alarm
//        start_alarm.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //setting calendar instance with the hour and minute that we have picked
//                //on the time picker
//                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
//                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
//
//                //get the string values of the hour and minute
//                int hour = alarm_timepicker.getHour();
//                int minute = alarm_timepicker.getMinute();
//
//                //convert the int values to string
//                String hour_string = String.valueOf(hour);
//                String minute_string = String.valueOf(minute);
//
//                //convert 24 hour time to 12 hour time
//                if (hour > 12){
//                    hour_string = String.valueOf(hour - 12);
//                }
//
//                if (minute < 10) {
//                    //10:7 -> 10:07
//                    minute_string = "0" + String.valueOf(minute);
//                }
//
//                //put in extra string into alarm_intent
//                //tells the clock that you put the alarm on button
//                alarm_intent.putExtra("extra","Alarm on");
//
//
//                //create a pendig intent that delays the intent
//                //until the specified calendar time
//                pending_intent =  PendingIntent.getBroadcast(AlarmActivity.this, 0, alarm_intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//
//                //set the alarm manager
//                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                        pending_intent);
//
//
//            }
//        });

        //initialise the end alarm button
        Button end_alarm = (Button) findViewById(R.id.end_alarm);

        //Create an onClick listener to turn off the alarm

        end_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cancel the alarm
                alarm_manager.cancel(pending_intent);

                //put extra string into alarm_intent
                //tells the clock that you pressed "end_alarm" button
                alarm_intent.putExtra("extra", "Alarm off");

                //stop alarm
                sendBroadcast(alarm_intent);
                Intent intent1 = new Intent(AlarmActivity.this, MapActivity.class);
                startActivity(intent1);
            }

        });

        //Initialise days selection
        CheckBox monday = (CheckBox) findViewById(R.id.monday);
        CheckBox tuesday = (CheckBox) findViewById(R.id.tuesday);
        CheckBox wednesday = (CheckBox) findViewById(R.id.wednesday);
        CheckBox thursday = (CheckBox) findViewById(R.id.thursday);
        CheckBox friday = (CheckBox) findViewById(R.id.friday);

        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.monday:
                        list.add(1);
                        break;
                    case R.id.tuesday:
                        list.add(2);
                        break;
                    case R.id.wednesday:
                        list.add(3);
                        break;
                    case R.id.thursday:
                        list.add(4);
                        break;
                    case R.id.friday:
                        list.add(5);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String frompd = intent.getStringExtra("notification");
        Log.d("Amit", "frompd " +frompd);
        if ("yes".equals(frompd)) {
            stopAlarm();
            Intent intent1 = new Intent(this, MapActivity.class);
            startActivity(intent1);
        }
    }

    private void stopAlarm(){
        for(int i=0;i<list.size();i++){
            alarmManager[i].cancel(intentArray.get(i));
            alarm_intent.putExtra("extra", "Alarm off");
            sendBroadcast(alarm_intent);
        }
    }

    public void onSave(View v) {
        EditText classname_text = (EditText) findViewById(R.id.classname);
        String classname = classname_text.getText().toString();

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
        calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
        int hour = alarm_timepicker.getHour();
        int minute = alarm_timepicker.getMinute();
        String hour_string = String.valueOf(hour);
        String minute_string = String.valueOf(minute);
        //convert 24 hour time to 12 hour time
        if (hour > 12) {
            hour_string = String.valueOf(hour - 12);
        }
        if (minute < 10) {
            minute_string = "0" + String.valueOf(minute);
        }

        //Intent alarm_intent = new Intent(context, Alarm_Receiver.class);
        alarm_intent.putExtra("extra", "Alarm on");
        pending_intent = PendingIntent.getBroadcast(AlarmActivity.this, 0, alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm_manager.set(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis() - (1000*60*30)), pending_intent);
        Switch switchButton = (Switch) findViewById(R.id.switchbutton);

        DataValues eventData = new DataValues(classname, hour_string, minute_string, switchButton.isChecked(),list);
        Log.d("SAGAR", "ClassName :" + classname + " Alarm Time: " + hour_string + ":" + minute_string + "   Preference:" + switchButton.isChecked());
        OnTimeDB.insertIntoDB(sqLiteDatabase);
        super.onBackPressed();
    }

    //Create an onClick listener to turn off the alarm

/*     Map   end_alarm.setOnClickListener(new View.OnClickListener() {
            Intent alarm_intent = new Intent(context,Alarm_Receiver.class);

            @Override
            public void onClick(View v) {
                //method to update the alarm text
                set_alarm_text("Alarm off!");

                //cancel the alarm
                for(int i=0;i<list.size();i++){
                    alarmManager[i].cancel(intentArray.get(i));
                    alarm_intent.putExtra("extra", "Alarm off");
                }

                //put extra string into alarm_intent
                //tells the clock that you pressed "end_alarm" button
//                alarm_intent.putExtra("extra", "Alarm off");

                //stop alarm
                sendBroadcast(alarm_intent);
            }

        });*/
}
