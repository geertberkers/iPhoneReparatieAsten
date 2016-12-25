package geert.berkers.iphonereparatieasten.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import geert.berkers.iphonereparatieasten.activitytest.GPSTestActivity;

/**
 * Created by Geert.
 */

public class GPSLocationListener implements LocationListener {


    public GPSLocationListener() {
    }

    @Override
    public void onLocationChanged(Location location) {

        String longitude = "Longitude: " + location.getLongitude();
        String latitude = "Latitude: " + location.getLatitude();

        System.out.println(longitude);
        System.out.println(latitude);

        GPSTestActivity.setLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        System.out.println("Status changed: " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        System.out.println("Provider enabled: " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        System.out.println("Provider disabled: " + s);
    }

}