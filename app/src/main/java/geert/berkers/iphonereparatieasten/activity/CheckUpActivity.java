package geert.berkers.iphonereparatieasten.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.activitytest.CameraTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.GPSTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.TestActivity;
import geert.berkers.iphonereparatieasten.enums.TestResult;
import geert.berkers.iphonereparatieasten.model.TestItem;

/**
 * Created by Geert.
 */

public class CheckUpActivity extends AppCompatActivity {

    private final static int CAMERA = 1;
    private final static int GPS = 2;
    private final static int WIFI = 3;

    List<TestItem> testItems;

    TestResult backCamera;
    TestResult frontCamera;
    TestResult gps;

    private Button btnReset;
    private Button btnCamera;
    private Button btnGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        initControls();
        testItems = new ArrayList<>();
    }

    private void initControls() {
        setTitle("Checkup");

        btnReset = (Button) findViewById(R.id.btnReset);
        btnCamera = (Button) findViewById(R.id.btnTestCamera);
        btnGPS = (Button) findViewById(R.id.btnTestGPS);

        btnReset.setVisibility(View.INVISIBLE);
    }

    public void testCamera(View view) {
        if (view.getId() == R.id.btnTestCamera) {
            if (checkCameraHardware(this)) {
                startCameraTest();
            } else {
                Toast.makeText(this, "No Camera available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void testGPS(View view) {
        if (view.getId() == R.id.btnTestGPS) {
            startGPSTest();
        }
    }

    public void reset(View view) {
        if (view.getId() == R.id.btnReset) {
            createResetAlertDialog().show();
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void startCameraTest() {
        Intent cameraIntent = new Intent(CheckUpActivity.this, CameraTestActivity.class);
        startActivityForResult(cameraIntent, CAMERA);
    }

    private void startGPSTest() {
        Intent gpsIntent = new Intent(CheckUpActivity.this, GPSTestActivity.class);
        startActivityForResult(gpsIntent, GPS);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                logButtonPress("Volume down");
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                logButtonPress("Volume up");
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private void logButtonPress(String button) {
        System.out.println(button);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mail) {
            //TODO: Gather results and send mail!
            System.out.println("Sent mail");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkup, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA:
                    handleCameraResult(data);
                    break;
                case GPS:
                    handleGPSResult(data);
                    break;
                case WIFI:
                    handleWiFiResult(data);
                    break;
                default:
                    break;
            }
        }

        btnReset.setVisibility(View.VISIBLE);
    }

    //TODO: Add versionCode for noPermission results!

    private void handleCameraResult(Intent data) {
        if (data != null) {
            // Get result
            boolean noPermission = data.getBooleanExtra("noPermission", false);
            boolean backCameraIsWorking = data.getBooleanExtra("backCameraIsWorking", false);
            boolean frontCameraIsWorking = data.getBooleanExtra("frontCameraIsWorking", false);

            // Log result
            System.out.println("Camera Permission: " + !noPermission);
            System.out.println("BackCamera: " + backCameraIsWorking);
            System.out.println("FrontCamera: " + frontCameraIsWorking);

            // Save result
            if(noPermission){
                backCamera = TestResult.NO_PERMISSION;
                frontCamera = TestResult.NO_PERMISSION;
            } else {
                backCamera = backCameraIsWorking ? TestResult.PASSED : TestResult.FAILED;
                frontCamera = frontCameraIsWorking ? TestResult.PASSED : TestResult.FAILED;
            }
            // Change background
            setButtonColor(btnCamera, (backCameraIsWorking && frontCameraIsWorking));
        }
    }

    private void handleGPSResult(Intent data) {
        if (data != null) {
            boolean noPermission = data.getBooleanExtra("noPermission", false);
            boolean gpsIsWorking = data.getBooleanExtra("gpsIsWorking", false);

            if(noPermission) {
                gps = TestResult.NO_PERMISSION;
            } else {
                gps = gpsIsWorking ? TestResult.PASSED : TestResult.FAILED;
            }

            setButtonColor(btnGPS, gpsIsWorking);
        }
    }

    private void handleWiFiResult(Intent data) {
        if (data != null) {
            boolean gpsIsWorking = data.getBooleanExtra("wifiIsWorking", false);
            gps = gpsIsWorking ? TestResult.PASSED : TestResult.FAILED;
            setButtonColor(btnGPS, gpsIsWorking);
        }
    }

    private void setButtonColor(Button button, boolean passed) {
        if (passed) {
            button.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        } else {
            button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
    }

    private AlertDialog.Builder createResetAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Reset resultaten?");
        alertDialogBuilder.setIcon(R.drawable.checkup);
        alertDialogBuilder.setMessage("Weet u zeker dat u alle test resultaten wilt verwijderen?");
        alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //TODO: Reset button colors
                btnCamera.getBackground().clearColorFilter();
                btnGPS.getBackground().clearColorFilter();

                // Reset TestResults of all tests
                for(TestItem testItem : testItems){
                    testItem.resetTestResult();
                }

                // Hide button
                btnReset.setVisibility(View.INVISIBLE);
                //TODO: hide button till 1 test is done
            }
        });
        alertDialogBuilder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return alertDialogBuilder;
    }

    public void testWifi(View view) {
        Intent testIntent = new Intent(CheckUpActivity.this, TestActivity.class);
        testIntent.putExtra("test", "WiFi");
        startActivityForResult(testIntent, WIFI);
    }
}
