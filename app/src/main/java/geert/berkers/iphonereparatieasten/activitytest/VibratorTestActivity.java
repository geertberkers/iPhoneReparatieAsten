package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
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
public class VibratorTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean vibratorTested;
    private boolean vibratorIsWorking;

    private Vibrator vibrator;
    private ImageView imageView;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.vibrator));
    }

    @Override
    protected void onDestroy() {
        if(vibrator != null){
            vibrator.cancel();
        }

        super.onDestroy();
    }

    private void initControls() {
        vibratorTested = false;
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_vibrator);
        txtQuestion.setText(R.string.question_test_vibrator_test);

        imageView = new ImageView(this);
        //TODO: Set image, and animation on vibration

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

        fabNotWorking.setVisibility(View.GONE);
    }

    private void isNotWorking() {
        vibratorIsWorking = false;
        setResult();
    }

    private void isWorking() {
        if (!vibratorTested) {
            startVibratorTest();
        } else {
            vibratorIsWorking = true;
            setResult();
        }
    }

    private void startVibratorTest() {
        vibratorTested = true;
        fabNotWorking.setVisibility(View.VISIBLE);

        txtInfo.setText(R.string.info_test_vibrating);
        txtQuestion.setText(R.string.question_test_vibator);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {400, 200};
        vibrator.vibrate(pattern, 0);
    }

    private void setResult(){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("vibratorIsWorking", vibratorIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }
}

