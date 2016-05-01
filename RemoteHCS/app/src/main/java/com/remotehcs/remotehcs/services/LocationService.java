package com.remotehcs.remotehcs.services;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by jeltierney on 5/1/16.
 */

public class LocationService implements LocationListener {

    private LocationManager locationManager;
    public Location location;
    public double longitude;
    public double latitude;
    public Boolean isNetworkEnabled;
    public Boolean isGPSEnabled;

    public LocationService(Context context) {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.d("Locaiton", "Permissions Not Granted");
            return;
        }

        Log.d("Locaiton", "Permissions Granted");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Lat = " + location.getLatitude() + "     Lon = " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals("GPS_PROVIDER")) {
            isGPSEnabled = false;
        }
        if (provider.equals("NETWORK_PROVIDER")) {
            isNetworkEnabled = false;
        }
        Log.d("Location", provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals("GPS_PROVIDER")) {
            isGPSEnabled = true;
        }
        if (provider.equals("NETWORK_PROVIDER")) {
            isNetworkEnabled = true;
        }
        Log.d("Location", provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location", "status");
    }
}