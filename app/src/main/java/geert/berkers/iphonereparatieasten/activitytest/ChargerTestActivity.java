package geert.berkers.iphonereparatieasten.activitytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
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

public class ChargerTestActivity extends AppCompatActivity {

    @SuppressWarnings("FieldCanBeLocal")
    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean chargerIsWorking;

    private ImageView imageView;
    private PowerConnectionReceiver powerBroadcastReceiver;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.charger));

        startPowerBroadcastReceiver();
    }

    @Override
    public void onPause() {
        unregisterReceiver(powerBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(powerBroadcastReceiver, filter);
        super.onResume();
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_charger);

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
    }

    private void isNotWorking() {
        chargerIsWorking = false;
        setResult();
    }

    private void isWorking() {
        chargerIsWorking = true;
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("chargerIsWorking", chargerIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    private void startPowerBroadcastReceiver() {
        powerBroadcastReceiver = new PowerConnectionReceiver();
    }

    public class PowerConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            if (checkConnected(plugged)) {
                handlePowerPluggedIn();
            } else {
                handleNoPowerPluggedIn();
            }
        }

        private boolean checkConnected(int plugged) {
            return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        }
    }

    private void handlePowerPluggedIn() {
        txtQuestion.setText(R.string.result_test_charger);
        fabWorking.setVisibility(View.VISIBLE);
        fabNotWorking.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.charger_plugged);
    }

    private void handleNoPowerPluggedIn() {
        txtQuestion.setText(R.string.question_test_charger);
        fabWorking.setVisibility(View.GONE);
        fabNotWorking.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.charger_unplugged);
    }
}
