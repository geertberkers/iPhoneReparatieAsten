package geert.berkers.iphonereparatieasten.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import geert.berkers.iphonereparatieasten.activitytest.GPSTestActivity;

/**
 * Created by Geert.
 */

public class GPSLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        GPSTestActivity.setLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        // Empty
    }

    @Override
    public void onProviderEnabled(String s) {
        // Empty
    }

    @Override
    public void onProviderDisabled(String s) {
        // Empty
    }

}