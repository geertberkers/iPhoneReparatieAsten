package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
public class CallSpeakerTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean callSpeakerIsPlaying;
    private boolean callSpeakerIsWorking;

    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.callspeaker));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_callspeaker);
        txtQuestion.setText(R.string.play_test_callspeaker);

        imageView = new ImageView(this);
        //TODO: Add speaker image

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

        callSpeakerIsPlaying = false;
        fabNotWorking.setVisibility(View.GONE);
    }

    private void isNotWorking() {
        callSpeakerIsWorking = false;
        setResult();
    }

    private void isWorking() {
        if (!callSpeakerIsPlaying) {
            startPlayingSound();
            txtQuestion.setText(R.string.question_test_callspeaker);
            fabNotWorking.setVisibility(View.VISIBLE);
        } else {
            callSpeakerIsWorking = true;
            setResult();
        }
    }

    private void startPlayingSound() {
        stopPlaying();
        callSpeakerIsPlaying = true;
        //TODO: Add speaker image animation

        mediaPlayer = MediaPlayer.create(this, R.raw.nova);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);

        AudioManager audioManager= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(false);
        audioManager.setStreamVolume(
                AudioManager.MODE_IN_COMMUNICATION,
                audioManager.getStreamMaxVolume(AudioManager.MODE_IN_COMMUNICATION),
                0
        );
        Resources res = getResources();
        AssetFileDescriptor afd = res.openRawResourceFd(R.raw.nova);
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    private void setResult(){
        stopPlaying();
        Intent intentMessage = new Intent();
        intentMessage.putExtra("callSpeakerIsWorking", callSpeakerIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

