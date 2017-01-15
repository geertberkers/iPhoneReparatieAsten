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

import geert.berkers.iphonereparatieasten.Information;
import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.activitytest.CameraTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.GPSTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.HeadsetTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.ChargerTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.TestActivity;
import geert.berkers.iphonereparatieasten.activitytest.TouchscreenTestActivity;
import geert.berkers.iphonereparatieasten.enums.TestResult;
import geert.berkers.iphonereparatieasten.model.TestItem;

/**
 * Created by Geert.
 */
public class CheckUpActivity extends AppCompatActivity {

    private final static int CAMERA = 1;
    private final static int HEADSET = 2;
    private final static int GPS = 3;
    private final static int TOUCHSCREEN = 4;
    private final static int LCD = 5;
    private final static int COMPAS = 6;
    private final static int CHARGER = 7;
    private final static int ON_OFF_BUTTON = 8;
    private final static int HOME_BUTTON = 9;
    private final static int VOLUME_BUTTONS = 10;
    private final static int SPEAKER = 11;
    private final static int MICROPHONE = 12;
    private final static int MULTITOUCH = 13;
    private final static int GYROSCOOP = 14;
    private final static int ACCELEROMETER = 15;
    private final static int WIFI = 16;

    List<TestItem> testItems;

    TestResult backCamera;
    TestResult frontCamera;
    TestResult headset;
    TestResult gps;
    TestResult touchscreen;
    TestResult charger;

    private Button btnReset;
    private Button btnCamera;
    private Button btnHeadset;
    private Button btnTouchscreen;
    private Button btnGPS;
    private Button btnCharger;
    private Button btnWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        initControls();
        createTestItemList();
    }

    private void createTestItemList() {
        testItems = new ArrayList<>();

        //TODO: Get Device information
        //TODO: Check if device has:
        // -  Camera (back/front)
        // -  Headset
        // -  GPS
        // -  TOUCHSCREEN
        // -  COMPAS
        // -  MICROPHONE (how much and where)
        // -  GYROSCOOP = 14;
        // -  ACCELEROMETER = 15;

        testItems.add(new TestItem("Camera"));

    }

    private void initControls() {
        setTitle("Checkup");

        btnReset = (Button) findViewById(R.id.btnReset);
        btnCamera = (Button) findViewById(R.id.btnTestCamera);
        btnHeadset = (Button) findViewById(R.id.btnTestHeadset);
        btnTouchscreen = (Button) findViewById(R.id.btnTestTouchscreen);
        btnGPS = (Button) findViewById(R.id.btnTestGPS);
        btnCharger = (Button) findViewById(R.id.btnTestCharger);

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

    public void testHeadset(View view) {
        if (view.getId() == R.id.btnTestHeadset) {
            startHeadsetTest();
        }
    }

    public void testTouchscreen(View view) {
        if (view.getId() == R.id.btnTestTouchscreen) {
            startTouchscreenTest();
        }
    }

    public void testCharger(View view) {
        if (view.getId() == R.id.btnTestCharger) {
            startChargerTest();
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

    private void startHeadsetTest() {
        Intent headsetIntent = new Intent(CheckUpActivity.this, HeadsetTestActivity.class);
        startActivityForResult(headsetIntent, HEADSET);
    }

    private void startTouchscreenTest() {
        Intent touchscreenTest = new Intent(CheckUpActivity.this, TouchscreenTestActivity.class);
        startActivityForResult(touchscreenTest, TOUCHSCREEN);
    }

    private void startGPSTest() {
        Intent gpsIntent = new Intent(CheckUpActivity.this, GPSTestActivity.class);
        startActivityForResult(gpsIntent, GPS);
    }

    private void startChargerTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, ChargerTestActivity.class);
        startActivityForResult(powerIntent, CHARGER);
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
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {Information.EMAIL});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Resultaten Checkup");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, createMessageFromResult());
            startActivity(Intent.createChooser(emailIntent, "Verstuur mail met..."));
        }

        return super.onOptionsItemSelected(item);
    }

    private String createMessageFromResult() {
        //TODO: Gather results and send mail!
        return "New message";
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
                case CAMERA:        handleCameraResult(data);       break;
                case HEADSET:       handleHeadsetResult(data);      break;
                case GPS:           handleGPSResult(data);          break;
                case TOUCHSCREEN:   handleTouchscreenResult(data);  break;
                case WIFI:          handleWiFiResult(data);         break;
                case CHARGER:       handleChargerResult(data);      break;
                default:            break;
            }
        }

        btnReset.setVisibility(View.VISIBLE);
    }

    private void handleCameraResult(Intent data) {
        if (data != null) {
            // Get result
            boolean noPermission = data.getBooleanExtra("noPermission", false);
            boolean backCameraIsWorking = data.getBooleanExtra("backCameraIsWorking", false);
            boolean frontCameraIsWorking = data.getBooleanExtra("frontCameraIsWorking", false);

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

    private void handleHeadsetResult(Intent data) {
        if (data != null) {
            boolean headsetIsWorking = data.getBooleanExtra("headsetIsWorking", false);
            headset = headsetIsWorking ? TestResult.PASSED : TestResult.FAILED;
            setButtonColor(btnHeadset, headsetIsWorking);
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

    private void handleTouchscreenResult(Intent data) {
        if (data != null) {
            boolean touchscreenIsWorking = data.getBooleanExtra("touchscreenIsWorking", false);
            touchscreen = touchscreenIsWorking ? TestResult.PASSED : TestResult.FAILED;
            setButtonColor(btnTouchscreen, touchscreenIsWorking);
        }
    }

    private void handleWiFiResult(Intent data) {
        if (data != null) {
            boolean wifiIsWorking = data.getBooleanExtra("wifiIsWorking", false);
            gps = wifiIsWorking ? TestResult.PASSED : TestResult.FAILED;
            setButtonColor(btnWifi, wifiIsWorking);
        }
    }

    private void handleChargerResult(Intent data) {
        if (data != null) {
            boolean chargerIsWorking = data.getBooleanExtra("chargerIsWorking", false);
            charger = chargerIsWorking ? TestResult.PASSED : TestResult.FAILED;
            setButtonColor(btnCharger, chargerIsWorking);
        }
    }

    private void setButtonColor(Button button, boolean passed) {
        button.getBackground().setColorFilter(passed? Color.GREEN : Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    private AlertDialog.Builder createResetAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.reset_test_results);
        alertDialogBuilder.setIcon(R.drawable.checkup);
        alertDialogBuilder.setMessage(R.string.confirmation);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //TODO: Reset button colors
                btnCamera.getBackground().clearColorFilter();
                btnGPS.getBackground().clearColorFilter();
                btnHeadset.getBackground().clearColorFilter();
                btnTouchscreen.getBackground().clearColorFilter();
                btnCharger.getBackground().clearColorFilter();

                // Reset TestResults of all tests
                for(TestItem testItem : testItems){
                    testItem.resetTestResult();
                }

                // Hide button
                btnReset.setVisibility(View.INVISIBLE);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
