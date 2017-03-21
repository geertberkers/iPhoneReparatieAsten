package geert.berkers.iphonereparatieasten.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.iphonereparatieasten.Information;
import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.activitytest.AccuTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.CallSpeakerTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.CameraTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.ChargerTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.CompassTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.FingerprintTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.FlashTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.GPSTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.HeadsetTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.HomeButtonTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.LCDTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.MultiTouchTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.OnOffButtonTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.SpeakerTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.TouchscreenTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.VibratorTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.VolumeControlsTestActivity;
import geert.berkers.iphonereparatieasten.activitytest.WiFiTestActivity;
import geert.berkers.iphonereparatieasten.adapter.TestItemAdapter;
import geert.berkers.iphonereparatieasten.enums.TestResult;
import geert.berkers.iphonereparatieasten.listeners.ClickListener;
import geert.berkers.iphonereparatieasten.listeners.RecyclerTouchListener;
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
    private final static int FLASH = 6;
    private final static int COMPASS = 7;
    private final static int CHARGER = 8;
    private final static int ACCU = 9;
    private final static int ON_OFF_BUTTON = 10;
    private final static int HOME_BUTTON = 11;
    private final static int FINGERPRINT = 12;
    private final static int VOLUME_CONTROLS = 13;
    private final static int SPEAKER = 14;
    private final static int CALL_SPEAKER = 15;
    private final static int VIBRATOR = 16;
    private final static int MICROPHONE = 17;
    private final static int MULTITOUCH = 18;
    private final static int GYROSCOPE = 19;
    private final static int ACCELEROMETER = 20;
    private final static int WIFI = 21;
    private final static int NFC = 22;

    private static final int FINGERPRINT_REQUEST_CODE = 999;

    private int index;
    private Button btnReset;
    private List<TestItem> testItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter testItemAdapter;
    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup);

        initControls();
        createTestItemList();
        initRecyclerView();

        //TODO: Save results on quit!
        //TODO: Load results from last time
    }

    private void initControls() {
        setTitle("Checkup");

        btnReset = (Button) findViewById(R.id.btnReset);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnReset.setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        testItemAdapter = new TestItemAdapter(testItems);
        recyclerView.setAdapter(testItemAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new ClickListener() {
                    @Override
                    public void onClick(int position) {
                        TestItem selectedItem = testItems.get(position);

                        switch (selectedItem.getRequestCode()) {
                            case CAMERA:            startCameraTest();          break;
                            case HEADSET:           startHeadsetTest();         break;
                            case GPS:               startGPSTest();             break;
                            case TOUCHSCREEN:       startTouchscreenTest();     break;
                            case LCD:               startLCDTest();             break;
                            case FLASH:             startFlashTest();           break;
                            case COMPASS:           startCompassTest();         break;
                            case CHARGER:           startChargerTest();         break;
                            case ACCU:              startAccuTest();            break;
                            case ON_OFF_BUTTON:     startOnOffButtonTest();     break;
                            case HOME_BUTTON:       startHomeButtonTest();      break;
                            case FINGERPRINT:       startFingerPrintTest();     break;
                            case VOLUME_CONTROLS:   startVolumeControlsTest();  break;
                            case SPEAKER:           startSpeakerTest();         break;
                            case CALL_SPEAKER:      startCallSpeakerTest();     break;
                            case VIBRATOR:          startVibratorTest();        break;
                            case MICROPHONE:        startMicrophoneTest();      break;
                            case MULTITOUCH:        startMultiTouchTest();      break;
                            case GYROSCOPE:         startGyroscopeTest();       break;
                            case ACCELEROMETER:     startAccelerometerTest();   break;
                            case WIFI:              startWiFiTest();            break;
                            case NFC:               startNFCTest();             break;
                            default:                                            break;
                        }
                    }
                })
        );
    }

    //region Methods to create TestList
    private void createTestItemList() {
        testItems = new ArrayList<>();

        addCameraTestItem();
        addHeadsetTest();
        addGPSTest();
        addTouchscreenTest();
        addLCDTest();
        addFlashTest();
        addCompassTest();
        addChargerTest();
        addAccuTest();
        addPowerButtonTest();
        addHomeButtonTest();
        addFingerPrintTest();
        addVolumeButtonsTest();
        addSpeakerTest();
        addCallSpeakerTest();
        addVibratorTest();
        addMicrophoneTest();
        addMultiTouchTest();
        addGyroscopeTest();
        addAccelerometerTest();
        addWifiTest();
        addNFCTest();
    }

    private void addCameraTestItem() {
        if (checkCameraHardware()) {
            testItems.add(new TestItem(
                    "Camera", CAMERA,
                    R.drawable.camera_untested,
                    R.drawable.camera_failed,
                    R.drawable.camera_passed)
            );
        }
    }

    private void addHeadsetTest() {
        //TODO: Check if device has 3.5mm jack / headset plug
        testItems.add(new TestItem(
                "Headset", HEADSET,
                R.drawable.headset_untested,
                R.drawable.headset_failed,
                R.drawable.headset_passed)
        );
    }

    private void addGPSTest() {
        if (checkGPSHardware()) {
            testItems.add(new TestItem(
                    "GPS", GPS,
                    R.drawable.gps_untested,
                    R.drawable.gps_failed,
                    R.drawable.gps_passed)
            );
        }
    }

    private void addTouchscreenTest() {
        testItems.add(new TestItem(
                "Touchscreen", TOUCHSCREEN,
                R.drawable.touchscreen_untested,
                R.drawable.touchscreen_failed,
                R.drawable.touchscreen_passed)
        );
    }

    private void addLCDTest() {
        testItems.add(new TestItem(
                "LCD", LCD,
                R.drawable.lcd_untested,
                R.drawable.lcd_failed,
                R.drawable.lcd_passed)
        );
    }

    private void addFlashTest() {
        if(checkFlashLightHardware()) {
            testItems.add(new TestItem(
                    "Zaklamp", FLASH,
                    R.drawable.flash_untested,
                    R.drawable.flash_failed,
                    R.drawable.flash_passed)
            );
        }
    }

    private void addCompassTest() {
        if (checkCompassHardware())
            testItems.add(new TestItem(
                    "Compas", COMPASS,
                    R.drawable.compass_untested,
                    R.drawable.compass_failed,
                    R.drawable.compass_passed)
            );
    }

    private void addChargerTest() {
        testItems.add(new TestItem(
                "Oplader", CHARGER,
                R.drawable.charger_untested,
                R.drawable.charger_failed,
                R.drawable.charger_passed)
        );
    }

    private void addAccuTest() {
        testItems.add(new TestItem(
                "Accu", ACCU,
                R.drawable.battery_untested,
                R.drawable.battery_failed,
                R.drawable.battery_passed)
        );
    }

    private void addPowerButtonTest() {
        testItems.add(new TestItem(
                "Aan/uit knop", ON_OFF_BUTTON,
                R.drawable.power_untested,
                R.drawable.power_failed,
                R.drawable.power_passed)
        );
    }

    private void addHomeButtonTest() {
        testItems.add(new TestItem(
                "Home knop", HOME_BUTTON,
                R.drawable.home_untested,
                R.drawable.home_failed,
                R.drawable.home_passed)
        );
    }

    private void addFingerPrintTest() {
        index = testItems.size();
        checkFingerprintHardware();
    }

    private void addVolumeButtonsTest() {
        //TODO: Check if device has alert slider
        testItems.add(new TestItem(
                "Volume knoppen", VOLUME_CONTROLS,
                R.drawable.volume_untested,
                R.drawable.volume_failed,
                R.drawable.volume_passed)
        );
    }

    private void addSpeakerTest() {
        testItems.add(new TestItem(
                "Speaker", SPEAKER,
                R.drawable.speaker_untested,
                R.drawable.speaker_failed,
                R.drawable.speaker_passed)
        );
    }

    private void addCallSpeakerTest() {
        if(!getResources().getBoolean(R.bool.isTab)) {
            testItems.add(new TestItem(
                    "Bel speaker", CALL_SPEAKER,
                    R.drawable.callspeaker_untested,
                    R.drawable.callspeaker_failed,
                    R.drawable.callspeaker_passed)
            );
        }
    }

    private void addVibratorTest() {
        testItems.add(new TestItem(
                "Vibrator", VIBRATOR,
                R.drawable.vibrate_untested,
                R.drawable.vibrate_failed,
                R.drawable.vibrate_passed)
        );
    }

    private void addMicrophoneTest() {
        //TODO: Check if device has multiple microphones
        testItems.add(new TestItem(
                "Microfoon", MICROPHONE,
                R.drawable.microphone_untested,
                R.drawable.microphone_failed,
                R.drawable.microphone_passed)
        );
    }

    private void addMultiTouchTest() {
        testItems.add(new TestItem(
                "Multitouch", MULTITOUCH,
                R.drawable.multitouch_untested,
                R.drawable.multitouch_failed,
                R.drawable.multitouch_passed)
        );
    }

    private void addGyroscopeTest() {
        //TODO: Check if device has Gyroscope
        testItems.add(new TestItem(
                "Gyroscoop", GYROSCOPE,
                R.drawable.gyroscope_untested,
                R.drawable.gyroscope_failed,
                R.drawable.gyroscope_passed)
        );
    }

    private void addAccelerometerTest() {
        //TODO: Check if device has accelerometer
        testItems.add(new TestItem(
                "Accelerometer", ACCELEROMETER,
                R.drawable.accelerometer_untested,
                R.drawable.accelerometer_failed,
                R.drawable.accelerometer_passed)
        );
    }

    private void addWifiTest() {
        //TODO: Check if device has wifi
        testItems.add(new TestItem(
                "WiFi", WIFI,
                R.drawable.wifi_untested,
                R.drawable.wifi_failed,
                R.drawable.wifi_passed)
        );
    }

    private void addNFCTest() {
        if (checkNFCHardware()) {
            testItems.add(new TestItem(
                    "NFC", NFC,
                    R.drawable.nfc_untested,
                    R.drawable.nfc_failed,
                    R.drawable.nfc_passed)
            );
        }
    }

    //TODO: Test NotificationLED
    //TODO: Test HeartRateSensor
    //TODO: Test Bluetooth?

    // endregion

    public void resetTestResults(View view) {
        if (view.getId() == R.id.btnReset) {
            createResetAlertDialog().show();
        }
    }

    /**
     * Check if this device has a camera
     *
     * @return true if it has a camera false if it doesn't
     */
    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Check if this device has a flashlight
     *
     * @return true if it has a flashlight false if it doesn't
     */
    private boolean checkFlashLightHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * Check if this device has GPS
     *
     * @return true if it has a GPS false if it doesn't
     */
    private boolean checkGPSHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    /**
     * Check if this device has a compass
     *
     * @return true if it has a compass false if it doesn't
     */
    @SuppressWarnings("deprecation")
    private boolean checkCompassHardware() {
        //TODO: Replace with newer version
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < deviceSensors.size(); i++) {
            if (deviceSensors.get(i).getType() == Sensor.TYPE_ORIENTATION) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if this device has NFC
     *
     * @return true if it has NFC false if it doesn't
     */
    private boolean checkNFCHardware() {
        return false;
//        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    /**
     * Check if this device has Fingerprint
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkFingerprintHardware() {
        fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, FINGERPRINT_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode == FINGERPRINT_REQUEST_CODE){
           if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
               checkFingerScannerHardware();
           }
       }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkFingerScannerHardware() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, FINGERPRINT_REQUEST_CODE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (fingerprintManager.isHardwareDetected()) {
                    testItems.add(index, new TestItem(
                            "Vingerafdruk scanner", FINGERPRINT,
                            R.drawable.fingerprint_untested,
                            R.drawable.fingerprint_failed,
                            R.drawable.fingerprint_passed)
                    );
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void startCameraTest() {
        Intent cameraIntent = new Intent(CheckUpActivity.this, CameraTestActivity.class);
        startActivityForResult(cameraIntent, CAMERA);
    }

    private void startHeadsetTest() {
        Intent headsetIntent = new Intent(CheckUpActivity.this, HeadsetTestActivity.class);
        startActivityForResult(headsetIntent, HEADSET);
    }

    private void startGPSTest() {
        Intent gpsIntent = new Intent(CheckUpActivity.this, GPSTestActivity.class);
        startActivityForResult(gpsIntent, GPS);
    }

    private void startTouchscreenTest() {
        Intent touchscreenTest = new Intent(CheckUpActivity.this, TouchscreenTestActivity.class);
        startActivityForResult(touchscreenTest, TOUCHSCREEN);
    }

    private void startLCDTest() {
        Intent lcdIntent = new Intent(CheckUpActivity.this, LCDTestActivity.class);
        startActivityForResult(lcdIntent, LCD);
    }

    private void startFlashTest() {
        Intent flashIntent = new Intent(CheckUpActivity.this, FlashTestActivity.class);
        startActivityForResult(flashIntent, FLASH);
    }

    private void startCompassTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, CompassTestActivity.class);
        startActivityForResult(powerIntent, COMPASS);
    }

    private void startChargerTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, ChargerTestActivity.class);
        startActivityForResult(powerIntent, CHARGER);
    }

    private void startAccuTest() {
        Intent accuIntent = new Intent(CheckUpActivity.this, AccuTestActivity.class);
        startActivityForResult(accuIntent, ACCU);
    }

    private void startOnOffButtonTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, OnOffButtonTestActivity.class);
        startActivityForResult(powerIntent, ON_OFF_BUTTON);
    }

    private void startHomeButtonTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, HomeButtonTestActivity.class);
        startActivityForResult(powerIntent, HOME_BUTTON);
    }

    private void startFingerPrintTest() {
        Intent fingerprintIntent = new Intent(CheckUpActivity.this, FingerprintTestActivity.class);
        startActivityForResult(fingerprintIntent, FINGERPRINT);
    }

    private void startVolumeControlsTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, VolumeControlsTestActivity.class);
        startActivityForResult(powerIntent, VOLUME_CONTROLS);
    }

    private void startSpeakerTest() {
        Intent fingerprintIntent = new Intent(CheckUpActivity.this, SpeakerTestActivity.class);
        startActivityForResult(fingerprintIntent, SPEAKER);
    }

    private void startCallSpeakerTest() {
        Intent fingerprintIntent = new Intent(CheckUpActivity.this, CallSpeakerTestActivity.class);
        startActivityForResult(fingerprintIntent, CALL_SPEAKER);
    }

    private void startVibratorTest() {
        Intent powerIntent = new Intent(CheckUpActivity.this, VibratorTestActivity.class);
        startActivityForResult(powerIntent, VIBRATOR);
    }

    private void startMicrophoneTest() {

    }

    private void startMultiTouchTest() {
        Intent multiTouchTest = new Intent(CheckUpActivity.this, MultiTouchTestActivity.class);
        startActivityForResult(multiTouchTest, MULTITOUCH);
    }

    private void startGyroscopeTest() {

    }

    private void startAccelerometerTest() {

    }

    private void startWiFiTest() {
        Intent testIntent = new Intent(CheckUpActivity.this, WiFiTestActivity.class);
        startActivityForResult(testIntent, WIFI);
    }

    private void startNFCTest() {
        //TODO: Implement or not? Most devices don't have NFC, and if they have most users don't use it
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mail) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Information.EMAIL});
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
                case CAMERA:            handleCameraResult(requestCode, data);          break;
                case HEADSET:           handleHeadsetResult(requestCode, data);         break;
                case GPS:               handleGPSResult(requestCode, data);             break;
                case TOUCHSCREEN:       handleTouchscreenResult(requestCode, data);     break;
                case LCD:               handleLCDResult(requestCode, data);             break;
                case FLASH:             handleFlashResult(requestCode, data);           break;
                case COMPASS:           handleCompassResult(requestCode, data);         break;
                case CHARGER:           handleChargerResult(requestCode, data);         break;
                case ACCU:              handleAccuResult(requestCode, data);            break;
                case ON_OFF_BUTTON:     handleOnOffButtonResult(requestCode, data);     break;
                case HOME_BUTTON:       handleHomeButtonResult(requestCode, data);      break;
                case FINGERPRINT:       handleFingerprintResult(requestCode, data);     break;
                case VOLUME_CONTROLS:   handleVolumeControlsResult(requestCode, data);  break;
                case SPEAKER:           handleSpeakerResult(requestCode, data);         break;
                case CALL_SPEAKER:      handleCallSpeakerResult(requestCode, data);     break;
                case VIBRATOR:          handleVibratorResult(requestCode, data);        break;
                case MICROPHONE:        handleMicrophoneResult(requestCode, data);      break;
                case MULTITOUCH:        handleMultitouchResult(requestCode, data);      break;
                case GYROSCOPE:         handleGyroscopeResult(requestCode, data);       break;
                case ACCELEROMETER:     handleAccelerometerResult(requestCode, data);   break;
                case WIFI:              handleWiFiResult(requestCode, data);            break;
                case NFC:               handleNFCResult(requestCode, data);             break;
                default:                                                                break;
            }

            btnReset.setVisibility(View.VISIBLE);
        }
    }

    private void handleCameraResult(int requestCode, Intent data) {
        if (data != null) {
            // Get result
            boolean noPermission = data.getBooleanExtra("noPermission", false);
            boolean backCameraIsWorking = data.getBooleanExtra("backCameraIsWorking", false);
            boolean frontCameraIsWorking = data.getBooleanExtra("frontCameraIsWorking", false);

            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {

                    //TODO: Check how to save multiple items!
                    // Save result
                    if (noPermission) {
                        testItem.setTestResult(TestResult.NO_PERMISSION);

                        //backCamera = TestResult.NO_PERMISSION;
                        //frontCamera = TestResult.NO_PERMISSION;
                    } else {
                        testItem.setTestResult((backCameraIsWorking && frontCameraIsWorking) ? TestResult.PASSED : TestResult.FAILED);
                        //backCamera = backCameraIsWorking ? TestResult.PASSED : TestResult.FAILED;
                        //frontCamera = frontCameraIsWorking ? TestResult.PASSED : TestResult.FAILED;
                    }

                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleHeadsetResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean headsetIsWorking = data.getBooleanExtra("headsetIsWorking", false);
                    testItem.setTestResult(headsetIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleGPSResult(int requestCode, Intent data) {
        if (data != null) {
            boolean noPermission = data.getBooleanExtra("noPermission", false);
            boolean gpsIsWorking = data.getBooleanExtra("gpsIsWorking", false);

            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    if (noPermission) {
                        testItem.setTestResult(TestResult.NO_PERMISSION);
                    } else {
                        testItem.setTestResult(gpsIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    }

                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleTouchscreenResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean touchscreenIsWorking = data.getBooleanExtra("touchscreenIsWorking", false);
                    testItem.setTestResult(touchscreenIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleLCDResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean lcdIsWorking = data.getBooleanExtra("lcdIsWorking", false);
                    testItem.setTestResult(lcdIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleFlashResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean flashIsWorking = data.getBooleanExtra("flashIsWorking", false);
                    testItem.setTestResult(flashIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleCompassResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean compassIsWorking = data.getBooleanExtra("compassIsWorking", false);
                    testItem.setTestResult(compassIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleChargerResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean chargerIsWorking = data.getBooleanExtra("chargerIsWorking", false);
                    testItem.setTestResult(chargerIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleAccuResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean accuDetailsAreWorking = data.getBooleanExtra("accuDetailsAreWorking", false);
                    testItem.setTestResult(accuDetailsAreWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleOnOffButtonResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean onOffButtonIsWorking = data.getBooleanExtra("onOffButtonIsWorking", false);
                    testItem.setTestResult(onOffButtonIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleHomeButtonResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean homeButtonIsWorking = data.getBooleanExtra("homeButtonIsWorking", false);
                    testItem.setTestResult(homeButtonIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleFingerprintResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean fingerprintIsWorking = data.getBooleanExtra("fingerprintIsWorking", false);
                    testItem.setTestResult(fingerprintIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleVolumeControlsResult(int requestCode, Intent data) {
        if (data != null) {
            boolean alertSliderIsWorking = data.getBooleanExtra("alertSliderIsWorking", false);
            boolean volumeUpIsWorking = data.getBooleanExtra("volumeUpIsWorking", false);
            boolean volumeDownIsWorking = data.getBooleanExtra("volumeDownIsWorking", false);

            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    //TODO: Check how to save multiple items!
                    //TODO: Handle result for alert slider!
                    testItem.setTestResult((volumeUpIsWorking && volumeDownIsWorking) ? TestResult.PASSED : TestResult.FAILED);
                }

                testItemAdapter.notifyDataSetChanged();
            }
        }
    }

    private void handleSpeakerResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean speakerIsWorking = data.getBooleanExtra("speakerIsWorking", false);
                    testItem.setTestResult(speakerIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleCallSpeakerResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean callSpeakerIsWorking = data.getBooleanExtra("callSpeakerIsWorking", false);
                    testItem.setTestResult(callSpeakerIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleVibratorResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean vibratorIsWorking = data.getBooleanExtra("vibratorIsWorking", false);
                    testItem.setTestResult(vibratorIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleMicrophoneResult(int requestCode, Intent data) {
    }

    private void handleMultitouchResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean multiTouchIsWorking = data.getBooleanExtra("multiTouchIsWorking", false);
                    testItem.setTestResult(multiTouchIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleGyroscopeResult(int requestCode, Intent data) {
    }

    private void handleAccelerometerResult(int requestCode, Intent data) {
    }

    private void handleWiFiResult(int requestCode, Intent data) {
        if (data != null) {
            for (TestItem testItem : testItems) {
                if (testItem.getRequestCode() == requestCode) {
                    boolean wifiIsWorking = data.getBooleanExtra("wifiIsWorking", false);
                    testItem.setTestResult(wifiIsWorking ? TestResult.PASSED : TestResult.FAILED);
                    testItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void handleNFCResult(int requestCode, Intent data) {
    }

    private AlertDialog.Builder createResetAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.reset_test_results);
        alertDialogBuilder.setIcon(R.drawable.checkup);
        alertDialogBuilder.setMessage(R.string.confirmation);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Reset TestResults of all tests
                for (TestItem testItem : testItems) {
                    testItem.resetTestResult();
                    testItemAdapter.notifyDataSetChanged();
                }

                // Hide button
                btnReset.setVisibility(View.GONE);
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
}
