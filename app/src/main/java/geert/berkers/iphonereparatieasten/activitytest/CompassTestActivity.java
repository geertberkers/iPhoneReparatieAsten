package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.views.CompassDrawView;

/**
 * Created by Geert.
 */
public class CompassTestActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView image;
    private CompassDrawView compassDrawView;

    @SuppressWarnings("FieldCanBeLocal")
    private TextView txtInfo;
    private TextView txtQuestion;

    private float currentDegree = 0f;
    private boolean compassIsWorking;

    private ArrayList<Float> trackList;
    private SensorManager mSensorManager;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.compass));
    }

    private void initControls() {
        compassDrawView = new CompassDrawView(this);
        trackList = new ArrayList<>();

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.test_info_compass);
        txtQuestion.setText(R.string.test_question_compass);


        ImageView background = new ImageView(this);
        background.setImageResource(R.drawable.compass_background_trans);

        image = new ImageView(this);
        image.setImageResource(R.drawable.compass_ball);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.removeAllViews();
        frame.addView(background);
        frame.addView(image);
        frame.addView(compassDrawView);

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
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void isNotWorking() {
        compassIsWorking = false;
        setResult();
    }

    private void isWorking() {
        compassIsWorking = true;
        setResult();
    }

    private void setResult(){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("compassIsWorking", compassIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Fix deprecation
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        // Create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        ra.setDuration(210);
        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degree;

        if(trackList.size() <= 1){
            int width = image.getMeasuredWidth();
            int height = image.getMeasuredHeight();
            compassDrawView.setOvalSize(width, height);
        }

        if(!trackList.contains(degree)){
            trackList.add(degree);
            compassDrawView.setDegrees(trackList);
            compassDrawView.invalidate();
        }

        if(trackList.size() > 360){
            txtQuestion.setText(R.string.result_test_compass);
            fabWorking.setVisibility(View.VISIBLE);
            fabNotWorking.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Empty. Not used
    }
}