package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

public class LCDTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean lcdIsWorking;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcd_test);

        initControls();
        setTitle("LCD");
    }
    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_lcd);
        txtQuestion.setText(R.string.question_test_lcd);

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

    private void startColorTest(View view, int colorResource){
        Intent colorTestIntent = new Intent(LCDTestActivity.this, ColorTestActivity.class);
        colorTestIntent.putExtra("color", colorResource);

        txtInfo.setVisibility(View.INVISIBLE);
        txtQuestion.setVisibility(View.INVISIBLE);

        fabWorking.setVisibility(View.INVISIBLE);
        fabNotWorking.setVisibility(View.INVISIBLE);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "lcd");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(colorTestIntent, options.toBundle());
        } else {
            startActivity(colorTestIntent);
        }
    }

    private void isNotWorking() {
        lcdIsWorking = false;
        setResult();
    }

    private void isWorking() {
        lcdIsWorking = true;
        setResult();
    }

    @Override
    protected void onResume() {
        super.onResume();


        txtInfo.setVisibility(View.VISIBLE);
        txtQuestion.setVisibility(View.VISIBLE);
        fabWorking.setVisibility(View.VISIBLE);
        fabNotWorking.setVisibility(View.VISIBLE);

    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("lcdIsWorking", lcdIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    public void showColor(View view) {

        int colorResource;
        switch (view.getId()){
            case R.id.lcdWhite: colorResource = R.color.white; break;
            case R.id.lcdBlue: colorResource = R.color.blue; break;
            case R.id.lcdGreen: colorResource = R.color.green; break;
            case R.id.lcdRed: colorResource = R.color.red; break;
            case R.id.lcdBlack: colorResource = R.color.black; break;
            default: colorResource = R.color.white; break;
        }

        startColorTest(view, colorResource);
    }
}

