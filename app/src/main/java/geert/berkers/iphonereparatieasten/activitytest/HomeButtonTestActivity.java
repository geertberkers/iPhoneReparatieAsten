package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.receivers.HomeButtonReceiver;
import geert.berkers.iphonereparatieasten.listeners.OnHomePressedListener;
import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */
@SuppressWarnings("FieldCanBeLocal")
public class HomeButtonTestActivity extends AppCompatActivity {

    private boolean homeButtonIsWorking;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    private HomeButtonReceiver mHomeButtonReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.home_button));
    }

    @Override
    public void onResume() {
        if(homeButtonIsWorking){
            fabWorking.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void initControls() {
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_homebutton);
        txtQuestion.setText(R.string.question_test_homebutton);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.home_button);

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
        homeButtonIsWorking = false;

        initHomeButtonWatcher();
    }

    private void initHomeButtonWatcher() {
        mHomeButtonReceiver = new HomeButtonReceiver(this);
        mHomeButtonReceiver.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                homeButtonIsWorking = true;
            }
        });
        mHomeButtonReceiver.startWatch();
    }

    private void isNotWorking() {
        homeButtonIsWorking = false;
        setResult();
    }

    private void isWorking() {
        setResult();
    }

    private void setResult() {
        mHomeButtonReceiver.stopWatch();
        Intent intentMessage = new Intent();
        intentMessage.putExtra("homeButtonIsWorking", homeButtonIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }
}

