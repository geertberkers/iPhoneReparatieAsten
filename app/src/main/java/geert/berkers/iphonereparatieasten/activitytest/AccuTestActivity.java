package geert.berkers.iphonereparatieasten.activitytest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
public class AccuTestActivity extends AppCompatActivity {

    private FrameLayout frame;

    private TextView txtHealth;
    private TextView txtPercent;
    private TextView txtPlugged;
    private TextView txtCharging;
    private TextView txtTechnology;
    private TextView txtTemp;
    private TextView txtVoltage;

    private ImageView resultHealth;
    private ImageView resultPercent;
    private ImageView resultPlugged;
    private ImageView resultCharging;
    private ImageView resultTechnology;
    private ImageView resultTemp;
    private ImageView resultVoltage;

    private boolean accuDetailsAreWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.accu));
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
        System.out.println("OnResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryInfoReceiver);
        super.onPause();
    }

    private void initControls() {
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_accu);
        txtQuestion.setText(R.string.question_test_accu);

        frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.removeAllViews();

        createInformationView();

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

        //TODO: Get battery information and update view
        //updateView();
    }

    private void createInformationView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.accu_result_layout, frame, false);
        frame.addView(view);

        txtHealth = (TextView) findViewById(R.id.txtHealth);
        txtPercent = (TextView) findViewById(R.id.txtPercentage);
        txtPlugged = (TextView) findViewById(R.id.txtPlugged);
        txtCharging = (TextView) findViewById(R.id.txtCharging);
        txtTechnology = (TextView) findViewById(R.id.txtTechnology);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtVoltage = (TextView) findViewById(R.id.txtVoltage);

        resultHealth = (ImageView) findViewById(R.id.resultHealth);
        resultPercent = (ImageView) findViewById(R.id.resultPercentage);
        resultPlugged = (ImageView) findViewById(R.id.resultPlugged);
        resultCharging = (ImageView) findViewById(R.id.resultCharging);
        resultTechnology = (ImageView) findViewById(R.id.resultTechnology);
        resultTemp = (ImageView) findViewById(R.id.resultTemp);
        resultVoltage = (ImageView) findViewById(R.id.resultVoltage);
    }

    private void isNotWorking() {
        accuDetailsAreWorking = false;
        setResult();
    }

    private void isWorking() {
        accuDetailsAreWorking = true;
        setResult();
    }

    private void setResult(){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("accuDetailsAreWorking", accuDetailsAreWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };

    @SuppressLint("SetTextI18n")
    private void updateBatteryData(Intent intent) {
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        if (present) {
            String healthValue;
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:                healthValue = "Cold";                   break;
                case BatteryManager.BATTERY_HEALTH_DEAD:                healthValue = "Dead";                   break;
                case BatteryManager.BATTERY_HEALTH_GOOD:                healthValue = "Good";                   break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:        healthValue = "Over Voltage";           break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:            healthValue = "Overheat";               break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE: healthValue = "Unspecified failure";    break;
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                default:                                                healthValue = "Unknown";                break;
            }

            if(healthValue.equals("Unknown")){
                resultHealth.setImageResource(R.drawable.ic_uncheck);
                resultHealth.setColorFilter(Color.RED);
            }else {
                txtHealth.setText(healthValue);
                resultHealth.setImageResource(R.drawable.ic_check);
                resultHealth.setColorFilter(Color.GREEN);
            }

            // Calculate Battery Percentage ...
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                txtPercent.setText(batteryPct + " %");
                resultPercent.setImageResource(R.drawable.ic_check);
                resultPercent.setColorFilter(Color.GREEN);
            } else {
                resultPercent.setImageResource(R.drawable.ic_uncheck);
                resultPercent.setColorFilter(Color.RED);
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            String pluggedValue;

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:   pluggedValue = "Wireless";  break;
                case BatteryManager.BATTERY_PLUGGED_USB:        pluggedValue = "USB";       break;
                case BatteryManager.BATTERY_PLUGGED_AC:         pluggedValue = "AC";        break;
                default:                                        pluggedValue = "None";      break;
            }

            txtPlugged.setText(pluggedValue);
            resultPlugged.setImageResource(R.drawable.ic_check);
            resultPlugged.setColorFilter(Color.GREEN);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String statusValue;

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:        statusValue = "Opladen";    break;
                case BatteryManager.BATTERY_STATUS_FULL:            statusValue = "Vol";        break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:         statusValue = "Onbekend";   break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:                                            statusValue = "Ontladen";   break;
            }
            txtCharging.setText(statusValue);
            resultCharging.setImageResource(R.drawable.ic_check);
            resultCharging.setColorFilter(Color.GREEN);

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

                if (!"".equals(technology)) {
                    txtTechnology.setText(technology);
                    resultTechnology.setImageResource(R.drawable.ic_check);
                    resultTechnology.setColorFilter(Color.GREEN);
                } else {
                    resultTechnology.setImageResource(R.drawable.ic_uncheck);
                    resultTechnology.setColorFilter(Color.RED);
                }
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                txtTemp.setText(temp + "°C");
                resultTemp.setImageResource(R.drawable.ic_check);
                resultTemp.setColorFilter(Color.GREEN);
            } else {
                resultTemp.setImageResource(R.drawable.ic_uncheck);
                resultTemp.setColorFilter(Color.RED);
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
                txtVoltage.setText(voltage + " mV");
                resultVoltage.setImageResource(R.drawable.ic_check);
                resultVoltage.setColorFilter(Color.GREEN);
            } else {
                resultVoltage.setImageResource(R.drawable.ic_uncheck);
                resultVoltage.setColorFilter(Color.RED);
            }
        }
    }
}

