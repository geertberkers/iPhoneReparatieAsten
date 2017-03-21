package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
@SuppressWarnings("FieldCanBeLocal")
public class VolumeControlsTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean volumeUpTested;
    private boolean volumeDownTested;
    private boolean volumeUpIsWorking;
    private boolean volumeDownIsWorking;

    private ImageView imageView;
    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.volume_controls));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.animation_volume_up);
        AnimationDrawable vibrationAnimation = (AnimationDrawable) imageView.getDrawable();
        vibrationAnimation.start();

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

        txtInfo.setText(R.string.info_test_volume_up);
        txtQuestion.setText(R.string.question_test_volume);

        volumeUpTested = false;
        volumeDownTested = false;
    }

    private void isNotWorking() {
        setResult();
    }

    private void isWorking() {
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
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
                if (!volumeUpTested) {
                    volumeUpTested = true;
                    volumeUpIsWorking = true;
                    txtInfo.setText(R.string.info_test_volume_down);

                    imageView.setImageResource(R.drawable.animation_volume_down);
                    AnimationDrawable vibrationAnimation = (AnimationDrawable) imageView.getDrawable();
                    vibrationAnimation.start();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (volumeUpTested && !volumeDownTested) {
                    volumeDownTested = true;
                    volumeDownIsWorking = true;

                    fabWorking.setVisibility(View.VISIBLE);
                    fabNotWorking.setVisibility(View.GONE);
                    txtQuestion.setText(R.string.volume_both_recognized);
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
}

