package com.amko0l.ontime.ontime.ui;

/**
 * Created by amko0l on 4/18/2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amko0l.ontime.ontime.R;
import com.amko0l.ontime.ontime.database.DataValues;
import com.amko0l.ontime.ontime.database.OnTimeDB;
import com.amko0l.ontime.ontime.helper.EventsListAdapter;
import com.amko0l.ontime.ontime.learning.AndroidLibSVM;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ArrayList<String>> items = null;
    Button stopButton;
    Button startButton;
    SQLiteDatabase sqLiteDatabase;
    OnTimeDB onTimeDB;
    Context context;

    int accuracy_per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        context = getBaseContext();

        isLocationOn();

        onTimeDB = new OnTimeDB(this, "OnTimeDB", null, 1);
        sqLiteDatabase = onTimeDB.getWritableDatabase();
        items = onTimeDB.getAllEventsTitle(sqLiteDatabase);
        if (items == null) {
            items = new ArrayList<ArrayList<String>>();
        }
        if (items.size() == 0) {
            ArrayList<String> event = new ArrayList<String>();
            event.add("Add Events by pressing Start Button");
            items.add(event);
        }
        mAdapter = new EventsListAdapter(items);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }
                    // LS ADDED
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        Log.d("Item Removed",items.get(viewHolder.getAdapterPosition()).get(0));
                        onTimeDB.deleteEvent(sqLiteDatabase,items.get(viewHolder.getAdapterPosition()).get(0));
                        items.remove(viewHolder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location myLocation = getLastKnownLocation();
                DataValues dv = new DataValues();
                Log.d("Amit", "dv is " + dv + "  " + myLocation);
                if(myLocation==null)
                    isLocationOn();
                try {
                    dv.setSrc_lat(String.valueOf(myLocation.getLatitude()));
                    dv.setSrc_long(String.valueOf(myLocation.getLongitude()));
                }catch (Exception e){
                    isLocationOn();
                }

                //// TODO: 4/21/2017 Amit SVM
                Log.d("Amit ", "get src lat and long " + myLocation.getLongitude() + "  " + myLocation.getLatitude());
            }
        });

        stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location myLocation = getLastKnownLocation();
                DataValues dv = new DataValues();
                Log.d("Amit", "dv is " + dv + "  " + myLocation);
                if(myLocation==null)
                    isLocationOn();
                try {
                    dv.setSrc_lat(String.valueOf(myLocation.getLatitude()));
                    dv.setSrc_long(String.valueOf(myLocation.getLongitude()));
                }catch (Exception e){
                    isLocationOn();
                }


                //// TODO: 4/21/2017 Amit SVM
                //Log.d("Amit ", "get dest lat and long " + myLocation.getLongitude() + "  " + myLocation.getLatitude());
                createNewEvent();
            }
        });

        onTimeDB = new OnTimeDB(this, "OnTimeDB", null, 1);
        sqLiteDatabase = onTimeDB.getWritableDatabase();

        onExistingTrainingClicked();
    }

    LocationManager mLocationManager;

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    protected void onResume() {
        super.onResume();

        items = onTimeDB.getAllEventsTitle(sqLiteDatabase);
        if (items == null) {
            items = new ArrayList<ArrayList<String>>();
        }
        if (items.size() == 0) {
            ArrayList<String> event = new ArrayList<String>();
            event.add("Add Events by pressing Start Button");
            items.add(event);
        }
        Log.d("SAGAR",""+items);
        mAdapter = new EventsListAdapter(items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void createNewEvent()
    {
        Intent intent = new Intent(this,AlarmActivity.class);
        startActivity(intent);
    }


    public void onExistingTrainingClicked() {
        AndroidLibSVM androidLibSVM = new AndroidLibSVM(this);
        accuracy_per = androidLibSVM.trainExisting();
    }

    public void isLocationOn(){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(("GPS is off"));
            dialog.setPositiveButton("Open network setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            dialog.show();
        }
    }
}
