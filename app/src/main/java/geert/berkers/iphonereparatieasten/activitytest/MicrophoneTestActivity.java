package geert.berkers.iphonereparatieasten.activitytest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

@SuppressWarnings("FieldCanBeLocal")
public class MicrophoneTestActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean microphoneIsWorking;
    private boolean recordingStarted = false;
    private boolean recordingPlayed = false;
    private boolean permissionToRecordAccepted = false;

    private ImageView imageView;
    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle("Microfoon");

        txtInfo.setText("Druk op de knop om geluid op te nemen");
        fabNotWorking.setVisibility(View.GONE);
        txtQuestion.setVisibility(View.INVISIBLE);
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.microphone);
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

        setFileName();
    }

    private void setFileName() {
        if(getExternalCacheDir() != null) {
            mFileName = getExternalCacheDir().getAbsolutePath();
        } else {
            mFileName = getFilesDir().getAbsolutePath();
        }
        mFileName += "/AudioTest.3gp";
    }

    private void isNotWorking() {
        microphoneIsWorking = false;
        setResult();
    }

    private void isWorking() {
        if (!recordingStarted) {
            onRecord();
        } else if (!recordingPlayed) {
            startPlaying();
        } else {
            microphoneIsWorking = true;
            setResult();
        }
    }

    private void setResult(){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("microphoneIsWorking", microphoneIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            createRecordAlertDialog().show();
        }
    }

    private AlertDialog.Builder createRecordAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.call_failed);
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_call);
        alertDialogBuilder.setMessage(R.string.no_call_permission);
        alertDialogBuilder.setPositiveButton(R.string.give_rights, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onRecord();
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

    private void onRecord() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            startRecording();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void startPlaying() {
        imageView.setImageResource(R.drawable.animation_speaker);
        final AnimationDrawable vibrationAnimation = (AnimationDrawable) imageView.getDrawable();
        vibrationAnimation.start();

        fabWorking.setVisibility(View.INVISIBLE);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            recordingPlayed = true;

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    vibrationAnimation.stop();
                    imageView.setImageResource(R.drawable.animation_speaker);

                    txtQuestion.setVisibility(View.VISIBLE);
                    txtQuestion.setText("Was het geluid goed?");

                    fabWorking.setVisibility(View.VISIBLE);
                    fabNotWorking.setVisibility(View.VISIBLE);
                    stopPlaying();
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();

        recordingStarted = true;
        txtInfo.setText("Aan het opnemen.");

        fabWorking.setVisibility(View.INVISIBLE);
        txtQuestion.setVisibility(View.VISIBLE);

        final int[] secondsLeft = {0};
        new CountDownTimer(5500, 100) {
            public void onTick(long millisUntilFinished) {
                if (Math.round((float)millisUntilFinished / 1000.0f) != secondsLeft[0]) {
                    secondsLeft[0] = Math.round((float)millisUntilFinished / 1000.0f);
                    if(secondsLeft[0] == 0){
                        txtInfo.setText("Druk op de knop om het geluid af te spelen.");
                        txtQuestion.setVisibility(View.INVISIBLE);
                        fabWorking.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.animation_speaker);
                    }
                    txtQuestion.setText(String.valueOf(secondsLeft[0]));
                }
            }

            public void onFinish() {
                stopRecording();

            }
        }.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

}

