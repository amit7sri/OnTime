package com.amko0l.ontime.ontime.ui;

/**
 * Created by amko0l on 4/18/2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> items = null;
    Button stopButton;
    Button startButton;
    SQLiteDatabase sqLiteDatabase;
    OnTimeDB onTimeDB;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        context = getBaseContext();

        onTimeDB = new OnTimeDB(this, "OnTimeDB", null, 1);
        sqLiteDatabase = onTimeDB.getWritableDatabase();
        items = onTimeDB.getAllEventsTitle(sqLiteDatabase);
        if (items == null) {
            items = new ArrayList<String>();
        }
        if (items.size() == 0) {
            items.add("Add Events by pressing Start Button");
        }
        mAdapter = new EventsListAdapter(items);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
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
                dv.setSrc_lat(String.valueOf(myLocation.getLatitude()));
                dv.setSrc_long(String.valueOf(myLocation.getLongitude()));

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
                dv.setDest_lat(String.valueOf(myLocation.getLatitude()));
                dv.setDest_long(String.valueOf(myLocation.getLongitude()));

                //// TODO: 4/21/2017 Amit SVM
                Log.d("Amit ", "get dest lat and long " + myLocation.getLongitude() + "  " + myLocation.getLatitude());
                createNewEvent();
            }
        });

        onTimeDB = new OnTimeDB(this, "OnTimeDB", null, 1);
        sqLiteDatabase = onTimeDB.getWritableDatabase();
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
        items = OnTimeDB.getAllEventsTitle(sqLiteDatabase);
        if(items == null){
            items = new ArrayList<String>();
        }
        if(items.size() == 0 ){
            items.add("Add Events by pressing Start Button");
        }
        mAdapter = new EventsListAdapter(items);
        mAdapter.notifyDataSetChanged();
    }

    private void createNewEvent()
    {
        Intent intent = new Intent(this,AlarmActivity.class);
        startActivity(intent);
    }

}
