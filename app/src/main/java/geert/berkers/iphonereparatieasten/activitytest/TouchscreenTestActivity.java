package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.views.PixelGridView;

/**
 * Created by Geert.
 */

public class TouchscreenTestActivity extends AppCompatActivity {

    private static final int DENSITY_SQUARE = 36;
    private boolean touchscreenIsWorking;
    private PixelGridView pixelGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float height = outMetrics.heightPixels - getActionBarHeight();
        float width = outMetrics.widthPixels;

        float pixels = DENSITY_SQUARE * density;

        int squareCountWidth = (int) (width / pixels + 0.5f);
        int squareCountHeight = (int) (height / pixels + 0.5f);

        pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(squareCountWidth);
        pixelGrid.setNumRows(squareCountHeight);

        setContentView(R.layout.activity_touchscreen_test);
        initControls();

        setTitle("Touchscreen");
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        if (getSupportActionBar() != null) {
            actionBarHeight = getSupportActionBar().getHeight();
        }
        if (actionBarHeight != 0) {
            return actionBarHeight;
        }

        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
        } else if (getTheme().resolveAttribute(geert.berkers.iphonereparatieasten.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    private void initControls() {
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_touschreen);
        txtQuestion.setText(R.string.question_test_touchscreen);

        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayoutTouchscreen);
        frame.removeAllViews();
        frame.addView(pixelGrid);

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
        touchscreenIsWorking = false;
        setResult();
    }

    private void isWorking() {
        touchscreenIsWorking = true;
        setResult();
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("touchscreenIsWorking", touchscreenIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

}

