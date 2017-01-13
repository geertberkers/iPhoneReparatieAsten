package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

public class TouchscreenTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean touchscreenIsWorking;

    private PixelGridView pixelGrid;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float height = outMetrics.heightPixels;
        float width  = outMetrics.widthPixels;

        float pixels = 48 * density;

        int squareCountWidth = (int) (width / pixels + 0.5f);
        int squareCountHeight = (int) (height / pixels + 0.5f);

        pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(squareCountWidth);
        pixelGrid.setNumRows(squareCountHeight);

        setContentView(R.layout.activity_touchscreen_test);
        initControls();

        setTitle("Touchscreen");
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText("Raak alle blokken aan!");
        txtQuestion.setText("Het lukt niet om alle bokken aan te raken");

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayoutTouchscreen);
        frame.removeAllViews();
        frame.addView(pixelGrid);

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
    }


    private void isNotWorking() {
        touchscreenIsWorking = false;
        setResult();
    }

    private void isWorking() {
        touchscreenIsWorking = true;
        setResult();
    }

    private void setResult(){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("touchscreenIsWorking", touchscreenIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

}

