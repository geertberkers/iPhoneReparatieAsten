package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
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
public class SpeakerTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean speakerIsTested;
    private boolean speakerIsPlaying;
    private boolean speakerIsWorking;

    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.speaker));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_speaker);
        txtQuestion.setText(R.string.question_test_speaker_play);

        imageView = new ImageView(this);
        imageView.setPadding(20, 20, 20, 20);
        imageView.setImageResource(R.drawable.speaker);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!speakerIsTested) {
                    isWorking();
                } else if (!speakerIsPlaying) {
                    startPlayingSound();
                    startAnimation();
                } else {
                    stopPlaying();
                    imageView.setImageResource(R.drawable.speaker);
                }
            }
        });
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

        speakerIsPlaying = false;
        fabNotWorking.setVisibility(View.GONE);
    }

    private void isNotWorking() {
        speakerIsWorking = false;
        setResult();
    }

    private void isWorking() {
        if (!speakerIsTested) {
            startPlayingSound();
            startAnimation();

            txtQuestion.setText(R.string.question_test_speaker);
            fabNotWorking.setVisibility(View.VISIBLE);
        } else {
            speakerIsWorking = true;
            setResult();
        }
    }

    private void startPlayingSound() {
        stopPlaying();
        speakerIsTested = true;
        speakerIsPlaying = true;

        mediaPlayer = MediaPlayer.create(this, R.raw.nova);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setStreamVolume(
                AudioManager.MODE_NORMAL,
                audioManager.getStreamMaxVolume(AudioManager.MODE_NORMAL),
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

    private void startAnimation() {
        imageView.setImageResource(R.drawable.animation_speaker);
        AnimationDrawable vibrationAnimation = (AnimationDrawable) imageView.getDrawable();
        vibrationAnimation.start();
    }

    private void setResult() {
        stopPlaying();
        Intent intentMessage = new Intent();
        intentMessage.putExtra("speakerIsWorking", speakerIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    private void stopPlaying() {
        speakerIsPlaying = false;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

