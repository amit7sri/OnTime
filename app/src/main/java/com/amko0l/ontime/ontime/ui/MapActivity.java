package com.amko0l.ontime.ontime.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amko0l.ontime.ontime.R;
import com.amko0l.ontime.ontime.database.DataValues;
import com.amko0l.ontime.ontime.learning.NaiveBayes;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



/**
 * Created by amko0l on 4/20/2017.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    ArrayList<LatLng> markerPoints;
    Location curreLocation;
    String walktext;
    String biketext;
    LatLng src;
    LatLng dest;
    LatLng latLngcoffee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markerPoints = new ArrayList<LatLng>();

        if (isGoogleServiceAvailable()) {
            //Toast.makeText(this, "Perferct ", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_map);
            initMap();
        } else {
            Toast.makeText(this, "Can't connect to play services ", Toast.LENGTH_LONG).show();
        }

    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
    }

    public boolean isGoogleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvalable = api.isGooglePlayServicesAvailable(this);
        if (isAvalable == ConnectionResult.SUCCESS)
            return true;
        else if (api.isUserResolvableError(isAvalable)) {
            Dialog dialog = api.getErrorDialog(this, isAvalable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to play services ", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        gotoLocation(33.417580, -111.934293, 15);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        Location myLocation = getLastKnownLocation();
        DataValues dv = new DataValues();
        Log.d("Amit", "map activity is " + dv + "  " + myLocation);
        src = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        markerPoints.add(src);

        latLngcoffee = new LatLng(33.3957792, -111.9230301);
        dest = new LatLng(33.4188716, -111.9347489);
        markerPoints.add(latLngcoffee);
        markerPoints.add(dest);

        getDirections();

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        MarkerOptions options1 = new MarkerOptions();
        MarkerOptions options2 = new MarkerOptions();

        // Setting the position of the marker
        options.position(src);
        options1.position(latLngcoffee);
        options2.position(dest);

        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mGoogleMap.addMarker(options);
        NaiveBayes nb = new NaiveBayes();
        ArrayList<Integer> svmlist = new ArrayList<>();
        svmlist.add(0); //home location
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int time = 0;
        if(hour<12)
            time = 0;
        else if(hour>12 && hour <4)
            time = 1;
        else
            time = 2;
        //0 for clear weather, 1 for rainy
        //0 for home location, 1 for away
        if(nb.isCoofee(1, 0, time)) {
            Toast.makeText(this, "Enjoy coffee on the way, you will reach your destination on time",Toast.LENGTH_LONG).show();
            mGoogleMap.addMarker(options1);
        }else
            Toast.makeText(this, "Hurry up for the class",Toast.LENGTH_LONG).show();
        mGoogleMap.addMarker(options2);


    }

    private void showDialog(){
        new AlertDialog.Builder(MapActivity.this)
                .setTitle("Time to reach destination")
                .setMessage("Bike: " +   biketext + "\n" + "Walk: " +walktext)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void getDirections(){
        Routing walkrouting = new Routing.Builder().travelMode(Routing.TravelMode.WALKING).withListener(walkingListener).waypoints(src, dest).build();

        walkrouting.execute();

        Routing bikerouting = new Routing.Builder().travelMode(Routing.TravelMode.BIKING).withListener(bikingListener).waypoints(src, dest).build();

        bikerouting.execute();
        //showDialog();

    }


    RoutingListener bikingListener = new RoutingListener() {
        @Override
        public void onRoutingFailure(RouteException e) {
            Log.d("Subbu", "Bike Failure");
            biketext = "Failed to get walking directions";
        }

        @Override
        public void onRoutingStart() {

        }

        @Override
        public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
            Log.d("Subbu", "biking success");
            if(arrayList.size() > 0){
                Route route = arrayList.get(0);
                biketext=route.getDurationText();
                Toast.makeText(MapActivity.this, "Time to reach by bike  " + biketext, Toast.LENGTH_SHORT).show();
            } else {
                biketext= "There are no walking options available for this route";
            }

        }

        @Override
        public void onRoutingCancelled() {

        }
    };

    RoutingListener walkingListener = new RoutingListener() {
        @Override
        public void onRoutingFailure(RouteException e) {
            Log.d("Subbu", "Walking Failed");
            walktext = "Failed to get walking directions";
        }

        @Override
        public void onRoutingStart() {

        }

        @Override
        public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
            Log.d("Subbu", "Walking Success");
            if(arrayList.size() > 0){
                Route route = arrayList.get(0);
                walktext=route.getDurationText();
                Toast.makeText(MapActivity.this, "Time to reach by walk  " + walktext, Toast.LENGTH_SHORT).show();
            } else {
                walktext = "There are no walking options available for this route";
            }
        }

        @Override
        public void onRoutingCancelled() {

        }
    };

    private void gotoLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(cameraUpdate);
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

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Amit // TODO: 4/2/2017
        //mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
            Toast.makeText(this, "Can't get curent location ", Toast.LENGTH_LONG).show();
        else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 12);
            mGoogleMap.animateCamera(update);
        }
    }
}