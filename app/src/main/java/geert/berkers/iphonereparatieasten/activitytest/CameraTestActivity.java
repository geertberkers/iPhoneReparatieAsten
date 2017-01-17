package geert.berkers.iphonereparatieasten.activitytest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.views.CameraPreview;
import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

@SuppressWarnings("deprecation")
public class CameraTestActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 1;

    private final static int BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    private final static int FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private Camera mCamera;
    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean paused;
    private boolean permission;

    private boolean backCameraTested;
    private boolean frontCameraTested;

    private boolean backCameraIsWorking;
    private boolean frontCameraIsWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

        initControls();
        setTitle(getString(R.string.camera));

        paused = false;
        permission = false;

        backCameraTested = false;
        frontCameraTested = false;

        if (isAndroidMOrAbove()) {
            askCameraPermission();
        } else {
            testCamera(BACK);
        }
    }

    @Override
    protected void onResume() {
        if (permission && paused) {
            testCamera(backCameraTested ? FRONT : BACK);
            paused = false;
        }
        super.onResume();

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

    private void testCamera(int facing) {
        mCamera = openCamera(facing);

        if (facing == BACK) {
            txtInfo.setText(R.string.info_test_camera_back);
            txtQuestion.setText(R.string.question_test_camera_back);
        } else {
            txtInfo.setText(R.string.info_test_camera_front);
            txtQuestion.setText(R.string.question_test_camera_front);
        }

        CameraPreview mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.frameLayout);

        preview.removeAllViews();
        preview.addView(mPreview);
    }

    /**
     * Check if API Level  23+
     */
    private boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private Camera openCamera(int facing) {
        releaseCamera();

        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == facing) {
                try {
                    camera = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        return camera;
    }

    private void askCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            permission = true;
            testCamera(BACK);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION) {
            handleCameraPermission(grantResults);
        }
    }

    private void handleCameraPermission(int[] grantResults) {
        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            permission = true;
            testCamera(BACK);
        } else {
            createCameraAlertDialog().show();
        }
    }

    private AlertDialog.Builder createCameraAlertDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.camera_failed);
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_camera);
        alertDialogBuilder.setMessage(R.string.no_camera_permission);
        alertDialogBuilder.setPositiveButton(R.string.give_rights, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                askCameraPermission();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setNoPermissionResult();
            }
        });

        return alertDialogBuilder;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
        paused = true;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void isNotWorking() {
        if (!backCameraTested) {
            backCameraTested = true;
            backCameraIsWorking = false;
            testCamera(FRONT);
        } else if (!frontCameraTested) {
            frontCameraTested = true;
            frontCameraIsWorking = false;
            setResult();
        }
    }

    private void isWorking() {
        if (!backCameraTested) {
            backCameraTested = true;
            backCameraIsWorking = true;
            testCamera(FRONT);
        } else if (!frontCameraTested) {
            frontCameraTested = true;
            frontCameraIsWorking = true;
            setResult();
        }
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("backCameraIsWorking", backCameraIsWorking);
        intentMessage.putExtra("frontCameraIsWorking", frontCameraIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    private void setNoPermissionResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("noPermission", true);
        setResult(RESULT_OK, intentMessage);
        finish();
    }
}