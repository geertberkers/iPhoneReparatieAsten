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
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
@SuppressWarnings({"deprecation", "FieldCanBeLocal"})
public class FlashTestActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 1;
    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean flashIsOn;
    private boolean flashIsWorking;

    private Camera camera;
    private ImageView imageView;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.flashlight));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_flashlight);
        txtQuestion.setText(R.string.info_question_flashlight);

        imageView = new ImageView(this);
        //TODO: Add flash image

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.removeAllViews();
        frame.addView(imageView);

        fabNotWorking = (FloatingActionButton) findViewById(R.id.fabNotWorking);
        fabNotWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotWorking();
            }
        });

        fabWorking = (FloatingActionButton) findViewById(R.id.fabWorking);
        fabWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWorking();
            }
        });

        flashIsOn = false;
        fabNotWorking.setVisibility(View.GONE);
    }

    private void isNotWorking() {
        flashIsWorking = false;
        setResult();
    }

    private void isWorking() {
        if (!flashIsOn) {
            if (isAndroidMOrAbove()) {
                askCameraPermission();
            } else {
                startFlashLight();
            }
        } else {
            flashIsWorking = true;
            setResult();
        }
    }

    /**
     * Check if API Level  23+
     */
    private boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void askCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            startFlashLight();
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
            startFlashLight();
        } else {
            createCameraAlertDialog().show();
        }
    }

    private AlertDialog.Builder createCameraAlertDialog() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.flashlight_failed);
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_camera);
        alertDialogBuilder.setMessage(R.string.no_flashlight_permission);
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

    private void startFlashLight() {
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        camera.stopPreview();

        flashIsOn = true;
        txtQuestion.setText(R.string.question_test_flashlight);
        fabNotWorking.setVisibility(View.VISIBLE);
    }

    private void stopFlashLight() {
        camera.release();
    }


    private void setResult() {
        stopFlashLight();
        Intent intentMessage = new Intent();
        intentMessage.putExtra("flashIsWorking", flashIsWorking);
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