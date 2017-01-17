package geert.berkers.iphonereparatieasten.activitytest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import geert.berkers.iphonereparatieasten.R;

/**
 * Created by Geert.
 */

@SuppressWarnings("deprecation")
public class WiFiTestActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtQuestion;

    private FrameLayout frame;

    private boolean wifiTested;
    private boolean wifiIsWorking;

    private String bssid;
    private String ipAddress;
    private String network;

    private FloatingActionButton fabWorking;
    private FloatingActionButton fabNotWorking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //TODO: Check if wifi is on!
        initControls();
        setTitle(getString(R.string.wifi));
    }

    private void initControls() {
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);

        txtInfo.setText(null);
        txtQuestion.setText(R.string.start_test_wifi);

        ImageView imageView = new ImageView(this);
        frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.removeAllViews();
        frame.addView(imageView);

        imageView.setImageResource(R.drawable.wifi);

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
        wifiIsWorking = false;
        setResult();
    }

    private void isWorking() {
        if (!wifiTested) {
            wifiTested = true;
            getWifiInformation();
            fabNotWorking.setVisibility(View.VISIBLE);

        } else {
            wifiIsWorking = true;
            setResult();
        }
    }

    private void setResult() {
        Intent intentMessage = new Intent();
        intentMessage.putExtra("wifiIsWorking", wifiIsWorking);
        setResult(RESULT_OK, intentMessage);
        finish();
    }

    private void getWifiInformation() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo != null) {
                bssid = getBSSID(wifiInfo);
                ipAddress = getIpAddress(wifiInfo);
                network = getNetwork(wifiInfo);

                txtInfo.setText(R.string.result_info_test_wifi);
                txtQuestion.setText(R.string.question_wifi_result);

                updateView();
            }
        } else {
            updateView();
            txtQuestion.setText(R.string.no_result_test_wifi);
            fabWorking.setVisibility(View.GONE);

        }
    }

    private void updateView() {
        frame.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.wifi_result_layout, frame, false);

        frame.addView(view);
        TextView txtNetwork = (TextView) view.findViewById(R.id.txtNetwork);
        TextView txtBSSID = (TextView) view.findViewById(R.id.txtBSSID);
        TextView txtIPAddress = (TextView) view.findViewById(R.id.txtIPAddress);

        ImageView resultNetwork = (ImageView) view.findViewById(R.id.resultNetwork);
        ImageView resultBSSID = (ImageView) view.findViewById(R.id.resultBSSID);
        ImageView resultIPAddress = (ImageView) view.findViewById(R.id.resultIPAddress);


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

    private String getBSSID(WifiInfo wifiInfo) {
        return wifiInfo.getBSSID();
    }

    private String getIpAddress(WifiInfo wifiInfo) {
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    private String getNetwork(WifiInfo wifiInfo) {
        return wifiInfo.getSSID();
    }
}

