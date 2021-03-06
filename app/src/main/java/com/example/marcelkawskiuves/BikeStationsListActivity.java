package com.example.marcelkawskiuves;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class BikeStationsListActivity extends AppCompatActivity implements LocationListener {

    private ListView stationsList;
    private Location deviceLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_stations_list);

        stationsList = findViewById(R.id.stationsList);
        this.setTitle("ValenBisi");

        final BikeStationsAdapter bikeStationsAdapter = new BikeStationsAdapter(this, deviceLocation, this);
        stationsList.setAdapter(bikeStationsAdapter);

        setLocationOrGetPermissions();
    }

    public ListView getStationsList() { return stationsList; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bike_stations_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                final BikeStationsAdapter newBikeStationsAdapter = new BikeStationsAdapter(this, deviceLocation, this);
                stationsList.setAdapter(newBikeStationsAdapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stationsList.setAdapter(new BikeStationsAdapter(this, deviceLocation, this));
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Provider","disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Provider","enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Status","changed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void setLocationOrGetPermissions() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            deviceLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }
}
