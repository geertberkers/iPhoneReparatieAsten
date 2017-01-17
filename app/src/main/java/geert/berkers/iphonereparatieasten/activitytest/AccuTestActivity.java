package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
@SuppressWarnings("FieldCanBeLocal")
public class AccuTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private boolean accuDetailsAreWorking;

    private FrameLayout frame;
    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initControls();
        setTitle(getString(R.string.accu));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(R.string.info_test_accu);
        txtQuestion.setText(R.string.question_test_accu);

        frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.wifi_result_layout, frame, false);
        frame.addView(view);

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

        //TODO: Get battery information and update view
        updateView();
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

    private void updateView() {

        TextView txtNetwork = (TextView) findViewById(R.id.txtNetwork);
        TextView txtBSSID = (TextView) findViewById(R.id.txtBSSID);
        TextView txtIPAddress = (TextView) findViewById(R.id.txtIPAddress);

        ImageView resultNetwork = (ImageView) findViewById(R.id.resultNetwork);
        ImageView resultBSSID = (ImageView) findViewById(R.id.resultBSSID);
        ImageView resultIPAddress = (ImageView) findViewById(R.id.resultIPAddress);

        String bssid = "Test";
        String ipAddress = "Test";
        String network = "Test";

        if (bssid != null && !bssid.equals("")) {
            txtBSSID.setText(bssid);
            resultBSSID.setImageResource(R.drawable.ic_check);
            resultBSSID.setColorFilter(Color.GREEN);
        } else {
            resultBSSID.setImageResource(R.drawable.ic_uncheck);
            resultBSSID.setColorFilter(Color.RED);
        }

        if (ipAddress != null && !ipAddress.equals("")) {
            txtIPAddress.setText(ipAddress);
            resultIPAddress.setImageResource(R.drawable.ic_check);
            resultIPAddress.setColorFilter(Color.GREEN);
        } else {
            resultIPAddress.setImageResource(R.drawable.ic_uncheck);
            resultIPAddress.setColorFilter(Color.RED);
        }

        if (network != null && !network.equals("")) {
            txtNetwork.setText(network);
            resultNetwork.setImageResource(R.drawable.ic_check);
            resultNetwork.setColorFilter(Color.GREEN);
        } else {
            resultNetwork.setImageResource(R.drawable.ic_uncheck);
            resultNetwork.setColorFilter(Color.RED);
        }


    }
}

