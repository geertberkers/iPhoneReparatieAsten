package geert.berkers.iphonereparatieasten.activitytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
public class VolumeControlsTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean volumeUpTested;
    private boolean volumeDownTested;
    private boolean alertSliderTested;

    private boolean volumeUpIsWorking;
    private boolean volumeDownIsWorking;
    private boolean alertSliderIsWorking;

    private RingerModeBroadcastReceiver ringerModeBroadcastReceiver;

    private ImageView imageView;
    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle("Volume knoppen");
        startRingerBroadcastReceiver();
    }

    @Override
    public void onPause() {
        unregisterReceiver(ringerModeBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(ringerModeBroadcastReceiver, filter);
        super.onResume();
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        //TODO: Handle alert slider if phone has one!
        txtInfo.setText("Verander de positie van de alert slider");
        txtQuestion.setText("Verandering van de alert slider is niet gedetecteerd");

        imageView = new ImageView(this);
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

        fabWorking.setVisibility(View.GONE);

        //TODO: Implement test for alert slider
        //TODO: Read status and set image
        alertSliderTested = true;
        alertSliderIsWorking = true;

        imageView.setImageResource(R.drawable.volume_up);
        txtInfo.setText("Druk op de volume omhoog knop");
        txtQuestion.setText("Het drukken op de volume knop is niet gedetecteerd");

        volumeUpTested = false;
        volumeDownTested = false;
    }

    private void startRingerBroadcastReceiver() {
        ringerModeBroadcastReceiver = new RingerModeBroadcastReceiver();
    }
    private void isNotWorking() {
        setResult();
    }

    private void isWorking() {
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("alertSliderIsWorking", alertSliderIsWorking);
        intentMessage.putExtra("volumeUpIsWorking", volumeUpIsWorking);
        intentMessage.putExtra("volumeDownIsWorking", volumeDownIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (alertSliderTested && !volumeUpTested) {
                    volumeUpTested = true;
                    volumeUpIsWorking = true;

                    imageView.setImageResource(R.drawable.volume_down);
                    txtInfo.setText("Druk op de volume omlaag knop");
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (volumeUpTested && !volumeDownTested) {
                    volumeDownTested = true;
                    volumeDownIsWorking = true;

                    fabWorking.setVisibility(View.VISIBLE);
                    fabNotWorking.setVisibility(View.GONE);
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public static void getRingerMode(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp", "Silent mode");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.i("MyApp", "Vibrate mode");
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.i("MyApp", "Normal mode");
                break;
        }
    }

    public static class RingerModeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                getRingerMode(context);
            }
        }
    }
}

