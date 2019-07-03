package com.example.sampletrialone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    public static final String MY_PREFS_NAME= "Map_Co_Ord";
    public static final int LOCATION_REFRESH_TIME= 999999;
    public static final float LOCATION_REFRESH_DISTANCE= (float) 0.01;
    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean fineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean courseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (fineLocationAccepted && courseLocationAccepted){
                        Toast.makeText(MainActivity.this, "Permission Granted, Now you can access location data .", Toast.LENGTH_LONG).show();
                        gpsLocationCall();}
                    else {

                        Toast.makeText(MainActivity.this, "Permission Denied, You cannot access location data .", Snackbar.LENGTH_LONG).show();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                    showMessageOKCancel("You need to allow access to both the permissions",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                                                                PERMISSION_REQUEST_CODE);
                                                    }
                                                }
                                            });
                                    return;
                                }
                            }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(checkPermission()) {

            gpsLocationCall();
        }
        else
        {
            requestPermission();

        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void gpsLocationCall() {

        SharedPreferences.Editor co_ord = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        co_ord.putString("LIDL_Kremline","48.8101P2.3570");
        co_ord.putString("Carrefour_Villejuif", "48.783111P2.368109");
        co_ord.putString("Ivry_shop","48.8113175P2.3851852000000235");

        GPSTracker mGPS = new GPSTracker(this);
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String curr_co_ordinates;


        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            curr_co_ordinates = mGPS.getLatitude() + "P" + mGPS.getLongitude();
            System.out.println("curr_co_ordinates");
            co_ord.putString("Current", curr_co_ordinates);
            co_ord.commit();
        } else {
            curr_co_ordinates = null;
            System.out.println("Unable");
        }

//        try {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                    LOCATION_REFRESH_DISTANCE, mLocationListener);
//        } catch (SecurityException e) {
//            Log.e("MainActivity", "Security Exception");
//        }


//        co_ord.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                SharedPreferences co_ord = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                Location current = new Location("");
                String[] latlongCurr = co_ord.getAll().get("Current").toString().split("P");
                current.setLatitude(Double.parseDouble(latlongCurr[0]));
                current.setLongitude(Double.parseDouble(latlongCurr[1]));

                Location lidl = new Location("");
                String[] latlongLidl = co_ord.getAll().get("LIDL_Kremline").toString().split("P");
                lidl.setLatitude(Double.parseDouble(latlongLidl[0]));
                lidl.setLongitude(Double.parseDouble(latlongLidl[1]));

                float distanceInMeters = current.distanceTo(lidl)/1000;
                BigDecimal bd = new BigDecimal(Float.toString(distanceInMeters));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                Toast.makeText(MainActivity.this, "Distance between the two locations is :"+bd.toString(), Toast.LENGTH_LONG).show();
                //below code opens google maps with the location set for both from and to as below 
                final Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?"
                                + "saddr="+ Double.parseDouble(latlongCurr[0])+","+Double.parseDouble(latlongCurr[1]) + "&daddr="+Double.parseDouble(latlongLidl[0])+","+Double.parseDouble(latlongLidl[1]) ));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);
//                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                intent.putExtra("name_key", "Swarna");
//                startActivity(intent);
//                finish();
                break;
        }
    }
}
