package geert.berkers.iphonereparatieasten.activitytest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
@SuppressWarnings("FieldCanBeLocal")
public class HeadsetTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean headsetIsWorking;

    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle("Headset");

    }

    @Override
    public void onPause() {
        unregisterReceiver(headsetBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headsetBroadcastReceiver, filter);
        super.onResume();
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        imageView = new ImageView(this);
        txtInfo.setText(R.string.info_test_headset);
        txtQuestion.setText(R.string.question_test_headset);
        imageView.setImageResource(R.drawable.headset_unplugged);

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
    }

    private void isNotWorking() {
        headsetIsWorking = false;
        setResult();
    }

    private void isWorking() {
        headsetIsWorking = true;
        setResult();
    }

    private void setResult() {
        stopPlaying();
        Intent intentMessage = new Intent();
        intentMessage.putExtra("headsetIsWorking", headsetIsWorking);
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

    private BroadcastReceiver headsetBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0: handleNoHeadsetPluggedIn(); break;
                    case 1: handleHeadsetPluggedIn();   break;
                    default:
                        System.out.println("Cannot find headset state");
                }
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void handleNoHeadsetPluggedIn() {
        txtInfo.setText(R.string.info_test_headset);
        txtQuestion.setText(R.string.question_test_headset);

        stopPlaying();
        fabWorking.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.headset_unplugged);
    }

    @SuppressLint("SetTextI18n")
    private void handleHeadsetPluggedIn() {
        txtInfo.setText(R.string.info_test_headset_play_sound);
        txtQuestion.setText(R.string.question_test_headset_hear_sound);

        stopPlaying();
        fabWorking.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.headset_plugged);

        mediaPlayer = MediaPlayer.create(HeadsetTestActivity.this, R.raw.nova);
        mediaPlayer.start();
    }
}
