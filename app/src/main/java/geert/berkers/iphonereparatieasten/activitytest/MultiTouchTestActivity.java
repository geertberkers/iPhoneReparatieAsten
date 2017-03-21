package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.views.MultitouchView;

/**
 * Created by Geert.
 */
public class MultiTouchTestActivity extends AppCompatActivity {

    private boolean multiTouchIsWorking;

    private MultitouchView multitouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        multitouchView = new MultitouchView(this);
        setContentView(R.layout.activity_touchscreen_test);
        initControls();

        setTitle("MultiTouch");
    }

    private void initControls() {
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.multitouch_3_fingers);
        txtQuestion.setText(R.string.multitouch_not_working);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayoutTouchscreen);
        frame.removeAllViews();
        frame.addView(multitouchView);

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
        fabWorking.setVisibility(View.GONE);
    }

    private void isNotWorking() {
        multiTouchIsWorking = false;
        setResult();
    }

    private void isWorking() {
        multiTouchIsWorking = true;
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("multiTouchIsWorking", multiTouchIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }
}

