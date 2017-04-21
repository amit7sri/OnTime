package com.amko0l.ontime.ontime.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amko0l.ontime.ontime.services.RingtonePlayingService;
import com.amko0l.ontime.ontime.services.TimeCheckerService;

public class Alarm_Receiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver.", "Yay!");

        //fetch extra string from the intent
        String get_your_string = intent.getExtras().getString("extra");

        Log .e("The key is ", get_your_string);

        //create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from main activity to the ring tone playing service
        service_intent.putExtra("extra", get_your_string);

        //start ringtone service
        context.startService(service_intent);

        Log.d("SAGAR","Calling TimeCheckerService");
        Intent timer_intent = new Intent(context, TimeCheckerService.class);
        context.startService(timer_intent);
        Log.d("SAGAR","Calling TimeCheckerService done");
    }
}
