package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.receivers.ScreenReceiver;

/**
 * Created by Geert.
 */

public class OnOffButtonTestActivity extends AppCompatActivity {

    private boolean onOffButtonIsWorking;

    private FloatingActionButton fabWorking;
    @SuppressWarnings("FieldCanBeLocal")
    private FloatingActionButton fabNotWorking;

    private ScreenReceiver screenBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle("Volume knoppen");
        startRingerBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        screenBroadcastReceiver.screenTurnedOff = false;
        screenBroadcastReceiver.screenIsOnAgain = false;

        unregisterReceiver(screenBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenBroadcastReceiver, filter);

        Handler handler = new Handler();
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                if (screenBroadcastReceiver.screenTurnedOff && screenBroadcastReceiver.screenIsOnAgain) {
                    fabWorking.setVisibility(View.VISIBLE);
                }
            }
        };

        handler.postDelayed(runner, 500);
        super.onResume();
    }

    private void initControls() {
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_powerbutton);
        txtQuestion.setText(R.string.question_test_powerbutton);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.power_button);

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

        onOffButtonIsWorking = false;
    }

    private void startRingerBroadcastReceiver() {
        screenBroadcastReceiver = new ScreenReceiver();
    }

    private void isNotWorking() {
        onOffButtonIsWorking = false;
        setResult();
    }

    private void isWorking() {
        onOffButtonIsWorking = true;
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("onOffButtonIsWorking", onOffButtonIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }
}

