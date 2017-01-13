package geert.berkers.iphonereparatieasten.activitytest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.listeners.GPSLocationListener;

/**
 * Created by Geert.
 */

@SuppressLint("StaticFieldLeak")
public class GPSTestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION = 1;

    private final static int MIN_DISTANCE_BETWEEN_UPDATES = 10;
    private final static int MIN_TIME_INTERVAL_BETWEEN_UPDATES = 1000;

    private static TextView txtInfo;
    private static TextView txtQuestion;

    private boolean gpsIsWorking;

    private static GoogleMap mMap;
    private LocationManager locationManager;
    private GPSLocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayout);
        LayoutInflater.from(this).inflate(R.layout.activity_maps, frame, true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map) ;
        mapFragment.getMapAsync(this);

        initControls();

        locationListener = new GPSLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (isAndroidMOrAbove()) {
            askLocationPermission();
        } else {
            testGPS();
        }
    }

    private void askLocationPermission() {
        int fineLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocationPermissionCheck != PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION);
        } else {
            testGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION:
                handleLocationPermission(grantResults);
                break;
            default:
                break;
        }
    }

    private void handleLocationPermission(int[] grantResults) {
        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            testGPS();
        } else {
            createLocationAlertDialog().show();
        }
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("MissingPermission")
    private void testGPS() {
        txtInfo.setText("Wachten op GPS signaal.\nHeb even geduld aub.");
        txtQuestion.setText("Klik op X als u na een minuut geen GPS hebt ontvangen.");

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME_INTERVAL_BETWEEN_UPDATES,
                MIN_DISTANCE_BETWEEN_UPDATES,
                locationListener);
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        FloatingActionButton fabNotWorking = (FloatingActionButton) findViewById(R.id.fabNotWorking);
        fabNotWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotWorking();
            }
        });

        FloatingActionButton fabWorking = (FloatingActionButton) findViewById(R.id.fabWorking);
        fabWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWorking();
            }
        });
    }

    private void isWorking() {
        gpsIsWorking = true;
        setResult();
    }

    private void isNotWorking() {
        gpsIsWorking = false;
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("gpsIsWorking", gpsIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    /**
     * Check if API Level  23+
     */
    private boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @SuppressLint("SetTextI18n")
    public static void setLocation(final Location location) {
        txtInfo.setText("Locatie ontvangen:");
        txtQuestion.setText("Is dit uw huidige locatie?");

        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(loc).title("Huidige locatie"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @SuppressWarnings("MissingPermission")
    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }

    private AlertDialog.Builder createLocationAlertDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Locatie ophalen mislukt");
        alertDialogBuilder.setIcon(R.drawable.ic_location);
        alertDialogBuilder.setMessage("Geen permissies toegestaan om de locatie op te vragen.");
        alertDialogBuilder.setPositiveButton("Geef rechten", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                askLocationPermission();
            }
        });
        alertDialogBuilder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setNoPermissionResult();
            }
        });

        return alertDialogBuilder;
    }

    private void setNoPermissionResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("noPermission", true);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
